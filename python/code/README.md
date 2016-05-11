# Python Introduce

python 少有的一种可以称得上即简单又功能强大的编程语言

python 代表简单主义思想的语言

```python
➜  python git:(master) python -V
Python 2.7.10
➜  python git:(master)
```

## python REPL

```
➜  python git:(master) python
Python 2.7.10 (default, Jul 14 2015, 19:46:27)
[GCC 4.2.1 Compatible Apple LLVM 6.0 (clang-600.0.39)] on darwin
Type "help", "copyright", "credits" or "license" for more information.
>>> 2**3 or pow(2, 3)
8
>>> x = input("x:")
x:5
>>> print "hello ", x
hello  5
>>> print 3
3
>>> print '3'
3
>>> 0xAF
175
>>> 010
8
>>> 10
10
>>> round(1.0/3.0) #四舍五入
0.0
>>> round(1.0/2.0)
1.0
>>> import math
>>> math.floor(32.9)
32.0
```

## run helloworld.py

```python
#!/usr/bin/python
# Filename : helloworld.py
print 'Hello World'
```

```
➜  code git:(master) ✗ ll
total 4
-rw-r--r-- 1 hp staff 69 May 11 15:49 helloworld.py
➜  code git:(master) ✗ python helloworld.py
Hello World
```
