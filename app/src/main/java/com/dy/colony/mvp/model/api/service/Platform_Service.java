/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dy.colony.mvp.model.api.service;

import com.dy.colony.mvp.model.entity.Platform_LoginBack;
import com.dy.colony.mvp.model.entity.Platform_LoginOutBack;
import com.dy.colony.mvp.model.entity.Platform_UploadBack;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ================================================
 * <p>
 * 快检服务
 * <p>
 * =============================================
 */
public interface Platform_Service {

    String URL = "http://fstest.chinafst.cn:9005/fst";
    String SUCCESS = "0X00000";
    String offLinetoken = "15a42fe518874966f9c551d3adf283d6";

    /**
     * 用户登录
     *
     * @return
     */
    @Headers({"Domain-Name: xxx"})
    @POST("/interfaces/userLogin/login")
    Observable<Platform_LoginBack> login(@Query("userName") String username,
                                         @Query("password") String password,
                                         @Query("deviceCode") String devicecode,
                                         @Query("softWareVersion") String softWareVersion,
                                         @Query("place") String place,
                                         @Query("ip") String ip);

    /**
     * 用户退出登录
     *
     * @return
     */
    @Headers({"Domain-Name: xxx"})
    @POST("/api/iUser/loginOut")
    Observable<Platform_LoginOutBack> loginOut(@Body RequestBody body);


    /**
     * 6.2.仪器上传(不校验样品和检测项目)
     * @param body
     * @return
     */
    @Headers({"Domain-Name: xxx"})
    @POST("/iDataChecking/uploadUcData")
    Observable<Platform_UploadBack> uploadUcData(
            @Body RequestBody body);


}
