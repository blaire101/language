**Python Learn Part**

[More_Info](http://www.kuqin.com/abyteofpython_cn/)

**Content List**

- 1. Python Introduce
  - 1.1 python REPL
  - 1.2 python helloworld.py
  - 1.3 python help()
  - 1.4 to python_string
  - 1.5 difference between input and raw_input
- 2. Python Preliminary program
  - 2.1 Operators and Expression
  - 2.2 python control flow
- 3. function
  - 3.1 local var
  - 3.2 global var
  - 3.3 func_key
  - 3.4 DocStrings
- 4. Module
  - 4.1 **sys** module
  - 4.2 from..import
  - 4.3 __name__
  - 4.4 dir()

# 1. Python Introduce

python 少有的一种可以称得上即简单又功能强大的编程语言

python 代表简单主义思想的语言

```python
➜  python git:(master) python -V
Python 2.7.10
➜  python git:(master)
```

## 1.1 python REPL

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
>>> round(1.0/3.0) #四舍五入
0.0
>>> round(1.0/2.0)
1.0
>>> import math
>>> math.floor(32.9)
32.0
>>> print r'C:\nowhere''\\'    # 解决最后一个字符是 '\' 的情况！ 完美解决  
C:\nowhere\  
>>> print u'hello, world'      # Unicode 字符串， 在python3.0中，所有的字符串都是 Unicode 字符串  
hello, world  
>>>
```

## 1.2 python helloworld.py

```python
#!/usr/bin/python
# Filename : helloworld.py
print 'Hello World'
```

## 1.3 python help()

```python
>>> help()

Welcome to Python 2.7!  This is the online help utility.

help> input
Help on built-in function input in module __builtin__:

input(...)
    input([prompt]) -> value

        Equivalent to eval(raw_input(prompt)).
        (END)
```


## 1.4 to python_string

- str
- repr
- backquote

```python
>>> '"hello world" she said'  
'"hello world" she said'  
>>> "hello world"  
'hello world'  
>>> 10000L  
10000L  
>>> print "hello world"  
hello world  
>>> print 10000L  
10000  
>>> print str("Hello world")  
Hello world  
>>> print str(10000L)  
10000  
```

## 1.5 input and raw_input

```python
>>> raw_input("shuru : ")  
shuru : 6  
'6'  
>>> input("shuru : ")  #默认为合法的python表达式  
shuru : 5  
5   
```

# 2. Python Preliminary program

```python
>>> i = 5
>>> i = i + 1
>>> print \
... i
6
>>> print i
6
>>>
```

## 2.1 Operators and Expression

[operator precedence](http://www.kuqin.com/abyteofpython_cn/ch05s03.html)

**Expression**

```python
#!/usr/bin/python
# Filename: expression.py

length = 5
breadth = 2
area = length * breadth
print 'Area is', area
print 'Perimeter is', 2 * (length + breadth)
```

## 2.2 python control flow

```python
#!/usr/bin/python
# Filename: while.py

number = 23
running = True

while running:
  guess = int(raw_input('Enter an integer : '))

  if guess == number:
    print 'Congratulations, you guessed it.' 
    running = False # this causes the while loop to stop
  elif guess < number:
    print 'No, it is a little higher than that' 
  else:
    print 'No, it is a little lower than that' 
else:
  print 'The while loop is over.' 
  # Do anything else you want to do here

print 'Done'
```

**break, continue**

```python
#!/usr/bin/python
# Filename: continue.py

while True:
  s = raw_input('Enter something : ')
  if s == 'quit':
    break
  if len(s) < 3:
    continue
  print 'Input is of sufficient length'
  # Do other kinds of processing here...
```

# 3. function

## 3.1 local var

```python
#!/usr/bin/python
# Filename: func_local.py

def func(x):
    print 'x is', x
    x = 2
    print 'Changed local x to', x

x = 50
func(x)
print 'x is still', x
```

Output

$ python func_local.py
x is 50
Changed local x to 2
x is still 50


## 3.2 global var

```python
#!/usr/bin/python
# Filename: func_global.py

def func():
  global x

  print 'x is', x
  x = 2
  print 'Changed local x to', x

x = 50
func()
print 'Value of x is', x
```

Output

$ python func_global.py
x is 50
Changed global x to 2
Value of x is 2


## 3.3 func_key

```python
#!/usr/bin/python
# Filename: func_key.py

def func(a, b=5, c=10):
  print 'a is', a, 'and b is', b, 'and c is', c

  func(3, 7)
  func(25, c=24)
  func(c=50, a=100)
```

Output


$ python func_key.py
a is 3 and b is 7 and c is 10
a is 25 and b is 5 and c is 24
a is 100 and b is 5 and c is 50


## 3.4 DocStrings

```python
# #!/usr/bin/python
# Filename: func_doc.py

def printMax(x, y):
  '''Prints the maximum of two numbers.

    The two values must be integers.'''
  x = int(x) # convert to integers, if possible
  y = int(y)

  if x > y:
    print x, 'is maximum'
  else:
    print y, 'is maximum'
    return y

printMax(3, 5)
print printMax.__doc__
```

output

```
$ python func_doc.py
5 is maximum
Prints the maximum of two numbers.

    The two values must be integers.
```

# 4. Module

## 4.1 sys 模块

```python
#!/usr/bin/python
# Filename: using_sys.py

import sys

print 'The command line arguments are:'
for i in sys.argv:
  print i

print '\n\nThe PYTHONPATH is', sys.path, '\n'
```

## 4.2 from..import

**yourself module**

```python
#!/usr/bin/python
# Filename: mymodule.py

def sayhi():
    print 'Hi, this is mymodule speaking.'

version = '0.1'

# End of mymodule.py
```

mymodule_demo.py

```python
#!/usr/bin/python
# Filename: mymodule_demo.py

import mymodule

mymodule.sayhi()
print 'Version', mymodule.version
```

from..import..

```python
#!/usr/bin/python
# Filename: mymodule_demo2.py

from mymodule import sayhi, version
# Alternative:
# from mymodule import *

sayhi()
print 'Version', version
```

## 4.3 \_\_name\_\_

```
#!/usr/bin/python
# Filename: using_name.py

if __name__ == '__main__':
    print 'This program is being run by itself'
else:
    print 'I am being imported from another module'
```

> 每个Python模块都有它的__name__，如果它是'__main__'，这说明这个模块被用户单独运行，我们可以进行相应的恰当操作。

## 4.4 dir()

```python
$ python
>>> import sys
>>> dir(sys) # get list of attributes for sys module
['__displayhook__', '__doc__', '__excepthook__', '__name__', '__stderr__',
'__stdin__', '__stdout__', '_getframe', 'api_version', 'argv',
'builtin_module_names', 'byteorder', 'call_tracing', 'callstats',
'copyright', 'displayhook', 'exc_clear', 'exc_info', 'exc_type',
'excepthook', 'exec_prefix', 'executable', 'exit', 'getcheckinterval',
'getdefaultencoding', 'getdlopenflags', 'getfilesystemencoding',
'getrecursionlimit', 'getrefcount', 'hexversion', 'maxint', 'maxunicode',
'meta_path','modules', 'path', 'path_hooks', 'path_importer_cache',
'platform', 'prefix', 'ps1', 'ps2', 'setcheckinterval', 'setdlopenflags',
'setprofile', 'setrecursionlimit', 'settrace', 'stderr', 'stdin', 'stdout',
'version', 'version_info', 'warnoptions']
>>> dir() # get list of attributes for current module
['__builtins__', '__doc__', '__name__', 'sys']
>>>
>>> a = 5 # create a new variable 'a'
>>> dir()
['__builtins__', '__doc__', '__name__', 'a', 'sys']
>>>
>>> del a # delete/remove a name
>>>
>>> dir()
['__builtins__', '__doc__', '__name__', 'sys']
>>>
```

> 输入的sys模块上使用dir。我们看到它包含一个庞大的属性列表。
>
> dir() , 默认地，它返回当前模块的属性列表。

