#!/usr/bin/env python
# -*- coding: UTF-8 -*-

import urllib2
 
response = urllib2.urlopen("http://www.baidu.com")
print response.read()
