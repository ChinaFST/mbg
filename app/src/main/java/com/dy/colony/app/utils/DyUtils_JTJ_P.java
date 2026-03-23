package com.dy.colony.app.utils;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.mvp.model.entity.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class DyUtils_JTJ_P {

    /*获取一段数据的波谷信息
     * origin 处理的原始数组
     * dValue 差值
     * point 点数
     * return : 该段波的信息二维数组
     * */
    public static int[][] getWaveInfo(double[] origin, double dValue, int point) {
        //定义比较器, (i+1) -i>0 为正
        //起始点
        int[] startPoint = new int[point + 1];
        //结束点
        int[] endPoint = new int[point + 1];
        //波谷
        int[] wave = new int[point * 2];
        //数组长度
        int len = origin.length;
        //所有波谷坐标集合
        ArrayList<Integer> boguPoint = new ArrayList<Integer>();

        //比较器赋值
        for (int i = 0; i < point + 1; i++) {
            if (i == 0) {
                startPoint[i] = 1;
            } else {
                startPoint[i] = -1;
            }

            if (i == point) {
                endPoint[i] = -1;
            } else {
                endPoint[i] = 1;
            }
        }
        //波谷比较器赋值
        for (int i = 0; i < wave.length; i++) {
            if (i < wave.length / 2) {
                wave[i] = -1;
            } else {
                wave[i] = 1;
            }
        }
        //定义一个原数据走势
        int[] origin1 = new int[len];
        for (int i = 1; i < len; i++) {
            if (origin[i] - origin[i - 1] > dValue) {
                origin1[i] = 1;
            } else if (origin[i - 1] - origin[i] > dValue) {
                origin1[i] = -1;
            } else {
                origin1[i] = 0;
            }
        }

        //找波谷
        for (int i = point; i < len - point; i++) {
            int[] temp = new int[point * 2];
            for (int j = 0; j < point * 2; j++) {
                temp[j] = origin1[j + i - point];
            }
            int arrSum = getArrSum(temp, wave);
            if (arrSum >= point * 2 - 1) {
                boguPoint.add(i);
                i = i + point;
            }
        }

        int[][] Sindex = new int[boguPoint.size()][3];


        //找出波谷起始点
        for (int i = 0; i < boguPoint.size(); i++) {
            int s = 0;
            if (boguPoint.get(i) - 20 < 0) {
                s = 0;
            } else {
                s = boguPoint.get(i) - 20;
            }
            Sindex[i][1] = boguPoint.get(i);
            for (int j = boguPoint.get(i) - point; j > s; j--) {
                int[] chage = new int[point + 1];
                for (int m = 0; m < point + 1; m++) {
                    chage[m] = origin1[m + j];
                }
                int arrSum = getArrSum(chage, startPoint);
                if (arrSum >= point) {
                    Sindex[i][0] = j;
                    break;
                }
            }


            if (Sindex[i][0] == 0) {

                if (boguPoint.get(i) - 20 < 0) {
                    Sindex[i][0] = 0;
                } else {
                    Sindex[i][0] = boguPoint.get(i) - 20;
                }
            }
        }
        //找出波谷结束点
        for (int i = 0; i < boguPoint.size(); i++) {
            //如果长度超过就指定为最后一个
            int s = 0;
            if (boguPoint.get(i) + 20 > len) {
                s = len;
            } else {
                s = boguPoint.get(i) + 20;
            }

            for (int j = boguPoint.get(i); j < s; j++) {
                int[] chage = new int[point + 1];
                for (int m = 0; m < point + 1; m++) {
                    chage[m] = origin1[j + m - point];
                }
                int arrSum = getArrSum(chage, endPoint);
                if (arrSum >= point) {
                    Sindex[i][2] = j;
                    break;
                }
            }

            if (Sindex[i][2] == 0) {
                if (boguPoint.get(i) + 20 > len) {
                    Sindex[i][2] = len - 1;
                } else {
                    Sindex[i][2] = boguPoint.get(i) + 20;
                }
            }
        }


        return Sindex;
    }


    //数组乘积和
    private static int getArrSum(int[] origin, int[] compare) {
        // TODO Auto-generated method stub
        if (origin.length != compare.length) {
            //System.out.println("--------------------数据错误");

        }
        double sum = 0;
        for (int i = 0; i < origin.length; i++) {
            if (origin[i] == compare[i]) {
                sum = sum + 1;
            } else if (origin[i] == 0) {
                sum = 0.91 + sum;
            } else {
                sum = sum - 1;
            }

            //sum=sum+origin[i]*compare[i];
        }
        //System.out.println("内"+sum);
        return (int) sum;
    }


    //传入参数 : usefulTemp 起始结束区间赋0的数组 ,m 指定多项式
    public static double[] duoXiangShi(double[] usefulTemp, int m) {

        double[][] a = new double[m][m];
        double[] b = new double[m];
        double[] c = new double[m];
        for (int i = 0; i < usefulTemp.length; i++) {
            if (usefulTemp[i] != 0) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k < m; k++) {
                        if (j + k == 0) {
                            a[j][k] = a[j][k] + 1;
                        } else {
                            if (i == 0) {
                                a[j][k] = a[j][k];
                            } else {
                                a[j][k] = a[j][k] + Math.pow(i * 0.1, j + k);
                            }

                        }
                    }
                    if (j == 0) {
                        b[j] = b[j] + usefulTemp[i];
                    } else {
                        if (i == 0) {
                            b[j] = b[j];
                        } else {
                            b[j] = b[j] + usefulTemp[i] * Math.pow(i * 0.1, j);
                        }


                    }


                }
            }
        }
        //a的逆矩阵


        a = getMatrix(a);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                c[i] = c[i] + a[j][i] * b[j];
            }
        }


        double[] temp3 = new double[usefulTemp.length];
        for (int i = 0; i < usefulTemp.length; i++) {
            for (int j = 0; j < m; j++) {
                temp3[i] = temp3[i] + c[j] * Math.pow(0.1 * i, j);
            }
        }
        return temp3;
    }

    public static double[][] getMatrix(double[][] arr) {
        double abs = getJuZhengMo(arr);
        if (abs == 0) {
            //System.out.println("数据错误");
            return null;
        }

        double[][] changeFunction = getChangeFunction(arr);
        changeFunction = getYuZiXizang(changeFunction);
        int cols = changeFunction.length;
        int rows = changeFunction[0].length;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if ((i + j) % 2 == 0) {
                    changeFunction[i][j] = changeFunction[i][j] / abs;
                } else {
                    changeFunction[i][j] = -changeFunction[i][j] / abs;
                }

            }
        }
        return changeFunction;
    }

    //获取矩阵转置
    private static double[][] getChangeFunction(double[][] arr) {
        // TODO Auto-generated method stub

        int length = arr[0].length;
        double[][] temp = new double[length][length];
        //r为行数
        for (int r = 0; r < length; r++) {
            //c为列数
            for (int c = 0; c < length; c++) {
                temp[c][r] = arr[r][c];
            }
        }
        return temp;
    }

    //求矩阵的模
    private static double getJuZhengMo(double[][] arr) {
        // TODO Auto-generated method stub
        double v = 0, val = 0;
        int length = arr[0].length;
        if (length == 1) {
            return arr[0][0];
        }
        int ans = 0;
        double[][] B = new double[length - 1][length - 1];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length - 1; j++) {
                for (int k = 0; k < length - 1; k++) {
                    ans = (k < i ? k : k + 1);
                    B[j][k] = arr[j + 1][ans];
                }
            }

            v = getJuZhengMo(B);
            if (i % 2 == 0) {
                val = val + arr[0][i] * v;
            } else {
                val = val - arr[0][i] * v;
            }
        }
        return val;
    }

    public static double[][] getYuZiXizang(double[][] arr) {

        int cols = arr.length;
        int rows = arr[0].length;
        double[][] temp = new double[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                double[][] temp1 = new double[cols - 1][rows - 1];
                for (int c = 0; c < cols - 1; c++) {
                    int c1 = c < i ? c : c + 1;
                    for (int r = 0; r < rows - 1; r++) {
                        int r1 = r < j ? r : r + 1;
                        temp1[c][r] = arr[c1][r1];
                    }
                }
                temp[i][j] = getJuZhengMo(temp1);
            }
        }
        return temp;
    }

    //获取有用数据
    public static double[] getUserFulData(double[] arr) {
        //通过定义的开始/结束点(34,205) 前后10个点 找出 最小值
		/*int dStart=34,dEnd=205,space=15,defultEndSpace=60,defultStartSpace=50;


		if(arr.length<dEnd){
			System.out.println("数据长度不够");
			return null;
		}
		double frontIndexValue= arr[dStart-10];
		double backIndexValue= arr[dEnd-10];
		int startIndex=dStart;

		for(int i=dStart;i<=40;i++){
			if(frontIndexValue>arr[i]){
				frontIndexValue=arr[i];
				startIndex=i;
			}
		}

		for(int i=dEnd+10;i>dEnd-10;i--){
			if(backIndexValue>arr[i]){
				backIndexValue=arr[i];
			}
		}

		//根据最小点找出有用波
		int startX=0,endX=0;
		for(int i=44;i<arr.length/2;i++){
			if(arr[i]<frontIndexValue){
				startX=i;
				break;
			}
		}
		for(int i=195;i>arr.length/2;i--){
			if(arr[i]<backIndexValue){
				endX=i;
				break;
			}
		}*/
        double[] useful;
        try {

            useful = new double[arr.length - 110];
            System.arraycopy(arr, 50, useful, 0, arr.length - 110);

        } catch (Exception e) {
            // TODO: handle exception
            useful = new double[200];
        }


        return useful;
    }

    /*返回两个波 波峰到起始点/结束点 斜率对应值的差值
     * 参数: first 有效波
     *  second 趋势线
     * */

    public static ArrayList<Double> doubles = new ArrayList<Double>();


    public static int[] index = new int[2];

    public static double[] getPointValue(double[] first, double[] duoxiangshi) {
        double[] result = new double[first.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i] / duoxiangshi[i];
            doubles.add(Double.valueOf(result[i]));
        }

		/*StringBuffer buffer2= new StringBuffer();
		for(int i=0;i<result.length;i++){
			buffer2.append(result[i]+",");
		}
		System.out.println("最终"+buffer2.toString());*/


        int[][] waveInfo = getWaveInfo(result, 0.0015, 8);
	/*	StringBuffer buffer4=new StringBuffer();
		for(int i=0;i<waveInfo.length;i++){
			buffer4.append(waveInfo[i][0]+"-"+waveInfo[i][1]+"-"+waveInfo[i][2]);
			buffer4.append("-----------");
		}
		System.out.println("最终波谷"+buffer4.toString());*/
        double[] value = new double[2];
        for (int i = 0; i < waveInfo.length; i++) {
            int start = waveInfo[i][0];
            int center = waveInfo[i][1];
            int end = waveInfo[i][2];
            //double pinjun =(result[end]-result[start])*(center-start)/(end-start)+result[start];
            double pinjun = (result[end] * (center - start) + result[start] * (end - center)) / (end - start) - result[center];
            pinjun = Math.log(1 + pinjun) / Math.log(2);


            //Math.log 是以e为底
            if (waveInfo[i][1] < result.length / 2) {
                if (value[0] < pinjun) {
                    value[0] = pinjun;
                    index[0] = center;
                }
            } else {
                if (value[1] < pinjun && center - index[0] > 40 && center - index[0] < 70) {
                    value[1] = pinjun;
                    index[1] = center;
                }
            }
        }

        return value;
    }


    /*
     * 小波分解
     * */
    public static double[] DB4WDT(double[] pBuf) {

        double[] h = {0.230378, 0.714847, 0.630881, -0.027984, -0.187035, 0.030841, 0.032883, -0.010597};
        double[] g = {-0.010597, -0.032883, 0.030841, 0.187035, -0.027984, -0.630881, 0.714847, -0.230378};
        double[] tmp;
        int i, j, n, pDLen, half, nLen = pBuf.length;

        if (pBuf == null) {
            return null;    //指针空返回NULL
        }
        if (nLen < 8) {
            return null;        //数据长度小于8返回NULL
        }

        if (nLen % 2 == 1) {
            tmp = new double[nLen + 7]; //malloc(sizeof(double) * (nLen + 7));
            pDLen = (nLen + 7);
        } else {
            tmp = new double[nLen + 6]; //malloc(sizeof(double) * (nLen + 6));
            pDLen = (nLen + 6);
        }

        half = pDLen / 2;
        for (i = 0; i < half; i++) {
            /*tmp数组初始化*/
            tmp[i] = 0;
            tmp[i + half] = 0;

            for (j = 0; j < 8; j++) {
                n = 2 * i + j - 6;

                if (n >= nLen) {
                    n = 2 * nLen - 1 - n;
                } else if (n < 0) {
                    n = -1 * n - 1;
                }
                tmp[i] += h[j] * pBuf[n];
                tmp[i + half] += g[j] * pBuf[n];
            }
        }
        return tmp;
    }

    /*
     * 重构
     * */

    public static double[] DB4IWDT(double[] pBuf) {


        double[] h1 = {-0.010597, 0.032883, 0.030841, -0.187035, -0.027984, 0.630881, 0.714847, 0.230378};
        //g1[k] = (-1)^k*h1[(1-k)]
        double[] g1 = {-0.230378, 0.714847, -0.630881, -0.027984, 0.187035, 0.030841, -0.032883, -0.010597};
        double[] tmp;
        int i, j, n, half, nLen = pBuf.length;
        if (pBuf == null) {
            return null;
        }
        if (nLen < 8) {
            return null;
        }
        half = nLen / 2;
        tmp = new double[nLen - 6];//malloc(sizeof(double)*(nLen-6));
        for (i = 0; i < nLen - 6; i++) {
            tmp[i] = 0;
            for (j = 0; j < 8; j++) {
                n = i + j - 1;
                if (n >= nLen) {
                    n = 2 * nLen - 1 - n;
                }
                if (n % 2 == 0) {
                    tmp[i] += h1[j] * pBuf[n / 2];
                } else {
                    tmp[i] += g1[j] * pBuf[half + n / 2];
                }
            }
        }
        return tmp;
    }

    public static double[] DBFLT(double[] db) {
        double[] rtn = new double[db.length / 2];
        for (int i = 0; i < rtn.length; i++) {
            rtn[i] = db[i];
        }
        return rtn;
    }

    /*/
       返回上升沿最后一个值的下标
     */
    private static int seach_up(int i, float[] list) {
        if (i < list.length - 1) {
            if (list[i] < list[i + 1]) {
                return seach_up(i + 1, list);
            } else {
                return i;
            }
        } else {
            return -1;
        }

    }

    /*/
     *返回最后一个下降沿的下标
     */
    private static int seach_down(int i, float[] list) {
        if (i < list.length - 1) { //是否到最后一个点
            if (list[i] > list[i + 1]) {  //比较后一个是不是比当前点小
                return seach_down(i + 1, list); //继续寻找下降沿结束点
            } else {
                return i; //返回下降沿最后一个点的下标
            }
        } else {
            return -1; //无效值
        }

    }

    /**
     * 默认CT线是在中线的两边
     * 极端情况 CT线处于中间的一边，前半部分或者后半部分，其中包括C线处于中线位置或者t线处于中线位置
     * 这种情况就不好判断CT线，
     * 解决思路
     * 1不设置中线这一说法，直接在检测曲线中找出两个最大值看做CT线 （存在问题，如果是Ct线有一个是没有的情况下会出现误判，)）
     * 2不设置中线 先找出两个面积最大的值看做CT线，然后再看CT线位置，C线超过中线位置某个值时则把C线看做T线（以线宽20为界限值））
     *
     * @param list
     * @return
     */
    public static double[] dyMath(float[] list) {
        double[] result = new double[4];
        List<Float> areas = new ArrayList<>(); //面积集合
        for (int i = 20; i < list.length - 20; i++) {
            RegressionLine line2 = new RegressionLine();
            line2.addDataPoint(new DataPoint(i - 10, (float) list[i - 10]));
            line2.addDataPoint(new DataPoint(i + 10, (float) list[i + 10]));
            float a1 = line2.getA1(); //斜率
            float a0 = line2.getA0(); // b 值
            //LogUtils.d("\n回归线公式:  y = " + a1 + "x + " + a0);
            float v = 0;
            for (int j = i - 10; j < i + 10; j++) {
                float v1 = a1 * j + a0;
                float v2 = v1 - list[j];
                if (v2 >= 0) {
                    v = v + v2;
                }
            }
            areas.add(v);
        }
        int size = areas.size();
        //寻找最大面积
        float max1 = 0;
        int max1_index = 0;
        for (int i = 0; i < size / 2; i++) {
            Float aFloat = areas.get(i);
            if (aFloat > max1) {
                max1_index = i;
                max1 = aFloat;
            }
        }

        float max2 = 0;
        int max2_index = 0;
        //C线太靠近中线的情况
        //T线太靠近中线甚至是处于C线区域的情况
        //C线太靠近中线甚至是处于T线区域的情况
        if (max1_index + 10 > size / 2) {
            for (int i = max1_index + 10; i < size; i++) {
                Float aFloat = areas.get(i);
                if (aFloat > max2) {
                    max2_index = i;
                    max2 = aFloat;
                }
            }
        } else {
            for (int i = size / 2; i < size; i++) {
                Float aFloat = areas.get(i);
                if (aFloat > max2) {
                    max2_index = i;
                    max2 = aFloat;
                }
            }
        }

        result[0] = max1_index;
        result[1] = max1;
        result[2] = max2_index;
        result[3] = max2;
        return result;
    }

    public static double[] dyMath_External(float[] list) {
        double[] result = new double[4];
        List<Float> areas = new ArrayList<>();
        for (int i = 5; i < list.length - 5; i++) {
            RegressionLine line2 = new RegressionLine();
            line2.addDataPoint(new DataPoint(i - 5, (float) list[i - 5]));
            line2.addDataPoint(new DataPoint(i + 5, (float) list[i + 5]));
            float a1 = line2.getA1(); //斜率
            float a0 = line2.getA0(); // b 值
            //LogUtils.d("\n回归线公式:  y = " + a1 + "x + " + a0);
            float v = 0;
            for (int j = i - 5; j < i + 5; j++) {
                float v1 = a1 * j + a0;
                float v2 = v1 - list[j];
                if (v2 >= 0) {
                    v = v + v2;
                }
            }
            areas.add(v);
        }

        int size = areas.size();
        //寻找最大面积
        float max1 = 0;
        int max1_index = 0;
        for (int i = 0; i < size / 2; i++) {
            Float aFloat = areas.get(i);
            if (aFloat > max1) {
                max1_index = i;
                max1 = aFloat;
            }
        }
        float max2 = 0;
        int max2_index = 0;
        for (int i = size / 2; i < size; i++) {
            Float aFloat = areas.get(i);
            if (aFloat > max2) {
                max2_index = i;
                max2 = aFloat;
            }
        }
        result[0] = max1_index;
        result[1] = max1;
        result[2] = max2_index;
        result[3] = max2;
        LogUtils.d(list);
        LogUtils.d(areas);
        LogUtils.d(result);
        return result;
    }


   /* public static float[] dyMath(float[] list) {
        List<Datas> dataslist = new ArrayList<>();
        for (int i = 0; i < list.length - 10; i++) { //遍历数组
            //寻找开始下降的点的下标
            if (list[i] > list[i + 1]) {
                //判断是不是下降沿
                int i1 = seach_down(i, list); //返回当前下降沿最后一个点的下标
                if (i1 == -1 || i1 == list.length) {
                    i1 = list.length - 1;  //寻找到了最后一个点
                }
                LogUtils.d(i1);
                // if ((list[i]-list[i1])>3){ //整个下降沿的下降幅度有没有超过 3 ，超过就是一个下降沿 ，否则就不是
                if ((i1 - i) > 18) {   //整个下降沿的长度 是否超过 18 个，超过就是下降沿，没超过就不是
                    LogUtils.d("下降沿" + i + "<->" + i1);
                    //寻找对应的上升沿
                    int i2 = seach_up(i1, list);
                    if (i2 == -1 || i2 == list.length) {
                        i2 = list.length - 1;
                    }
                    LogUtils.d(i2);
                    if ((i2 - i1) > 10) {   //上升沿是否大于18 大于就是 上升沿 小于就不是
                        LogUtils.d("上升沿" + i1 + "<->" + i2);
                        //判断峰值是不是在ct线范围内
                        if ((i1 < right1 - 20 && i1 > left1 - 20)) {   //i1为中间点，即下降沿最后一个点，上升沿的起始点
                            dataslist.add(new Datas("t", i, i1, i2));
                            i = i2;  //继续往后寻找上升下降沿
                            continue;
                        }
                        if ((i1 < right - 20 && i1 > left - 20)) {
                            dataslist.add(new Datas("c", i, i1, i2));
                            i = i2;
                            continue;
                        }

                    }
                }
                // i=i1;
                //  continue;
                // }
                i = i1;
                continue;
            }

        }

        List<Datas> cdata = new ArrayList<>();
        List<Datas> tdata = new ArrayList<>();
        for (int i = 0; i < dataslist.size(); i++) {
            Datas datas = dataslist.get(i);
            String value = datas.getValue();
            switch (value) {
                case "c":
                    cdata.add(datas);
                    break;
                case "t":
                    tdata.add(datas);
                    break;
            }
        }

        //寻找c最大下区间最大的值和下标
        Float[] cfloats = new Float[cdata.size()];
        if (cdata.size() > 1) {
            for (int i = 0; i < cdata.size(); i++) {
                int start = cdata.get(i).getStart();
                int center = cdata.get(i).getCentre();
                float v = list[start] - list[center];
                cfloats[i] = v;
            }
            float max = 0;
            int maxindex = 0;
            for (int i = 0; i < cfloats.length; i++) {
                if (cfloats[i] > max) {
                    maxindex = i;
                }
            }

            Datas e = cdata.get(maxindex);
            cdata.clear();
            cdata.add(e);

        }
        //寻找t最大下区间最大的值和下标
        Float[] tfloats = new Float[tdata.size()];
        if (tdata.size() > 1) {
            for (int i = 0; i < tdata.size(); i++) {
                int start = tdata.get(i).getStart();
                int center = tdata.get(i).getCentre();
                float v = list[start] - list[center];
                tfloats[i] = v;
            }
            float max = 0;
            int maxindex = 0;
            for (int i = 0; i < tfloats.length; i++) {
                if (tfloats[i] > max) {
                    maxindex = i;
                }
            }
            Datas e = tdata.get(maxindex);
            tdata.clear();
            tdata.add(e);

        }

        float[] v = new float[2];
        if (cfloats.length < 1) {
            v[0] = 0;
        } else {
            //计算c面积
            Datas datas = cdata.get(0);
            int centre = datas.getCentre();
            int i = centre - 20;
            int i2 = centre + 20;
            float a = (list[i2] - list[i]) / (i2 - i);
            float b = list[i] - a * i;
            float cv = 0;
            for (int j = i; j < i2; j++) {
                float vc = a * j + b;
                float v1 = vc - list[j];
                cv = cv + v1;
            }
            v[0] = cv;
        }

        if (tfloats.length < 1) {
            v[1] = 0;
        } else {
            //计算t面积
            Datas datast = tdata.get(0);
            int centret = datast.getCentre();
            int it = centret - 20;
            int i2t = centret + 20;
            float at = (list[i2t] - list[it]) / (i2t - it);
            float bt = list[it] - at * it;
            float tv = 0;
            for (int j = it; j < i2t; j++) {
                float vt = at * j + bt;
                float v1 = vt - list[j];
                tv = tv + v1;
            }
            v[1] = tv;
        }

        return v;

    }*/


}
