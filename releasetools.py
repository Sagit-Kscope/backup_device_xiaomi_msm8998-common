# Copyright (C) 2009 The Android Open Source Project
# Copyright (c) 2011, The Linux Foundation. All rights reserved.
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

import hashlib
import common
import re

def FullOTA_InstallBegin(info):
  input_zip = info.input_zip
  AddImage(info, "RADIO", input_zip, "super_dummy.img", "/tmp/super_dummy.img");
  info.script.AppendExtra('package_extract_file("install/bin/flash_super_dummy.sh", "/tmp/flash_super_dummy.sh");')
  info.script.AppendExtra('set_metadata("/tmp/flash_super_dummy.sh", "uid", 0, "gid", 0, "mode", 0755);')
  info.script.AppendExtra('run_program("/tmp/flash_super_dummy.sh");')
  return

def AddImage(info, dir, input_zip, basename, dest):
  path = dir + "/" + basename
  if path not in input_zip.namelist():
    return
