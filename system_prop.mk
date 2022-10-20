#
# Copyright (C) 2018 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Radio
PRODUCT_SYSTEM_PROPERTIES += \
    DEVICE_PROVISIONED=1 \
    keyguard.no_require_sim=true \
    persist.dbg.volte_avail_ovr=1 \
    persist.dbg.vt_avail_ovr=1 \
    persist.dbg.wfc_avail_ovr=1 \
    persist.radio.multisim.config=dsds \
    persist.rmnet.data.enable=true \
    persist.sys.fflag.override.settings_network_and_internet_v2=true \
    persist.sys.fflag.override.settings_provider_model=false \
    il.subscription.types=NV,RUIM \
    ro.com.android.dataroaming=false \
    ro.telephony.call_ring.multiple=false \
    ro.telephony.default_cdma_sub=0 \
    ro.telephony.default_network=22,20 \
    telephony.lteOnCdmaDevice=1

# RCS
PRODUCT_SYSTEM_PROPERTIES += \
    persist.rcs.supported=1
    
# Zram
PRODUCT_SYSTEM_PROPERTIES += \
    ro.zram.mark_idle_delay_mins=60 \
    ro.zram.first_wb_delay_mins=1440 \
    ro.zram.periodic_wb_delay_hours=24
    
# DPM
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.dpm.feature=1 \
    persist.vendor.dpm.nsrm.bkg.evt=3955
    
# Data
PRODUCT_SYSTEM_PROPERTIES += \
    ro.vendor.use_data_netmgrd=true \
    persist.vendor.data.iwlan.enable=true \
    persist.vendor.data.mode=concurrent
    
# TimeService
PRODUCT_SYSTEM_PROPERTIES += \
    persist.timed.enable=true
    