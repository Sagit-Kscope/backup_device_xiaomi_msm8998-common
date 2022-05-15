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
    persist.sys.fflag.override.settings_network_and_internet_v2=true \
    persist.sys.fflag.override.settings_provider_model=false

# Gesture
PRODUCT_SYSTEM_PROPERTIES += \
    qemu.hw.mainkeys=0
    
# Zram
PRODUCT_SYSTEM_PROPERTIES += \
    ro.zram.mark_idle_delay_mins=60 \
    ro.zram.first_wb_delay_mins=1440 \
    ro.zram.periodic_wb_delay_hours=24