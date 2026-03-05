#include <jni.h>

#include <android/log.h>
#include <android/bitmap.h>

#include <string.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <fcntl.h>              /* low-level i/o */
#include <unistd.h>
#include <errno.h>
#include <malloc.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/mman.h>
#include <sys/ioctl.h>

#include <asm/types.h>          /* for videodev2.h */

#include <linux/videodev2.h>
#include <linux/usbdevice_fs.h>

#define  LOG_TAG    "WangZX"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define CLEAR(x) memset (&(x), 0, sizeof (x))

/*#define IMG_WIDTH 1280
#define IMG_HEIGHT 720*/

#define ERROR_LOCAL -1
#define SUCCESS_LOCAL 0


/* Header for class com_dayuan_usbcamera_UvcCameraPreview */

#define _Included_com_dayuan_usbcamera_UvcCameraPreview
#ifdef __cplusplus
extern "C" {
#endif

struct buffer {
    void *start;
    size_t length;
};

static char dev_name[16];
static int fd = -1;
struct buffer *buffers = NULL;
static unsigned int n_buffers = 0;

int camerabase = -1;

int *rgb = NULL;
int *ybuf = NULL;

int yuv_tbl_ready = 0;
int y1192_tbl[256];
int v1634_tbl[256];
int v833_tbl[256];
int u400_tbl[256];
int u2066_tbl[256];

int errnoexit(const char *s);

int xioctl(int fd, int request, void *arg);

int checkCamerabase(void);

int opendevice(int videoid);

int initdevice(void);

int initmmap(void);

int startcapturing(void);

int readframeonce(void);

int readframe(void);

void processimage(const void *p);

int stopcapturing(void);

int uninitdevice(void);

int closedevice(void);

void yuyv422toABGRY(unsigned char *src);


/*
 * Class:     com_dy_colony_usbcamera_UvcCameraPreview
 * Method:    prepareCamera
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_dy_colony_usbcamera_UvcCameraPreview_prepareCamera
        (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     com_dy_colony_usbcamera_UvcCameraPreview
 * Method:    prepareCameraWithBase
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_com_dy_colony_usbcamera_UvcCameraPreview_prepareCameraWithBase
        (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     com_dy_colony_usbcamera_UvcCameraPreview
 * Method:    processCamera
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_dy_colony_usbcamera_UvcCameraPreview_processCamera
        (JNIEnv *, jobject);

/*
 * Class:     com_dy_colony_usbcamera_UvcCameraPreview
 * Method:    stopCamera
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_dy_colony_usbcamera_UvcCameraPreview_stopCamera
        (JNIEnv *, jobject);

/*
 * Class:     com_dy_colony_usbcamera_UvcCameraPreview
 * Method:    pixeltobmp
 * Signature: (Landroid/graphics/Bitmap;)V
 */
JNIEXPORT void JNICALL Java_com_dy_colony_usbcamera_UvcCameraPreview_pixeltobmp
        (JNIEnv *, jobject, jobject);
#ifdef __cplusplus
}
#endif
