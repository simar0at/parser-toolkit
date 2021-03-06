#!/usr/bin/python
# -*- coding: utf-8 -*-

#
# Copyright 2011 The Open Source Research Group,
#                University of Erlangen-Nürnberg
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#import re

#hideRx = re.compile('(enter|exit).*?( ... )')

silent = -1
indent = 0
for line in open('trace.txt', 'r').readlines():
  if line.startswith('exit'):
    indent -= 1

  if False: #hideRx.search(line) != None:
    if silent == -1:
      silent = indent
    elif silent == indent:
      silent = -1

  if silent == -1 or silent == indent:
    if line.startswith('enter') or line.startswith('exit') or line.startswith('lookup') or indent < 1:
      print ' ' * (2 * indent) + line,
    else:
      print '! ' + ' ' * (2 * (indent - 1)) + line,

  if line.startswith('enter'):
    indent += 1
