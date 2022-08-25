LOCAL_PATH := $(call my-dir)
ifeq ($(TARGET_DEVICE),$(filter $(TARGET_DEVICE),chiron sagit))
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_ANDROID_LIBRARIES := \
    androidx.core_core \
    androidx.appcompat_appcompat \
    androidx.preference_preference

LOCAL_STATIC_JAVA_LIBRARIES := \
    androidx.annotation_annotation

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_PACKAGE_NAME := XiaomiParts
LOCAL_REQUIRED_MODULES := XiaomiDirac
LOCAL_CERTIFICATE := platform
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res
LOCAL_USE_AAPT2 := true

package_resource_overlays := $(strip \
    $(wildcard $(foreach dir, $(PRODUCT_PACKAGE_OVERLAYS), \
      $(addprefix $(dir)/, packages/apps/XiaomiParts/res))) \
    $(wildcard $(foreach dir, $(DEVICE_PACKAGE_OVERLAYS), \
      $(addprefix $(dir)/, packages/apps/XiaomiParts/res))))

LOCAL_RESOURCE_DIR := $(package_resource_overlays) $(LOCAL_RESOURCE_DIR)

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_DEX_PREOPT := true

include frameworks/base/packages/SettingsLib/common.mk

include $(BUILD_PACKAGE)

endif
