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
- 5. Datastruct
  - 5.1 List
  - 5.2 Tuple
  - 5.3 Dict
  - 5.4 Seq
- 6. I/O
  - 6.1 File
  - 6.2 储存与取储存
- 7. Exception
  - 7.1 try...except
  - 7.2 try...finally
- 8. Python more
  - 8.1 list_comprehension
  - 8.2 function 接收 tuple和list
  - 8.3 lambda 表达式
  - 8.4 exec和eval语句
  - 8.5 assert 语句
  - 8.6 repr 函数

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

# 5. Datastruct

## 5.1 List

```python
#!/usr/bin/python
# Filename: using_list.py

# This is my shopping list
shoplist = ['apple', 'mango', 'carrot', 'banana']

print 'I have', len(shoplist),' .'

for item in shoplist:
  print item,

print("\n")

shoplist.append('rice')
print 'My shopping list is now', shoplist

shoplist.sort()
print 'Sorted shopping list is', shoplist

print 'The first item I will buy is', shoplist[0]
olditem = shoplist[0]
del shoplist[0]
print 'I bought the', olditem
print 'My shopping list is now', shoplist
```

output

➜  code git:(master) ✗ ./using_list.py
I have 4  .
apple mango carrot banana

My shopping list is now ['apple', 'mango', 'carrot', 'banana', 'rice']
Sorted shopping list is ['apple', 'banana', 'carrot', 'mango', 'rice']
The first item I will buy is apple
I bought the apple
My shopping list is now ['banana', 'carrot', 'mango', 'rice']


## 5.2 Tuple


**tuple element polymorphic**

```python
#!/usr/bin/python
# Filename: using_tuple.py

zoo = ('wolf', 'elephant', 'penguin')
print 'Number of animals in the zoo is', len(zoo)

new_zoo = ('monkey', 'dolphin', zoo)
print 'Number of animals in the new zoo is', len(new_zoo)
print 'All animals in new zoo are', new_zoo
print 'Animals brought from old zoo are', new_zoo[2]
print 'Last animal brought from old zoo is', new_zoo[2][2]
```

output

$ python using_tuple.py
Number of animals in the zoo is 3
Number of animals in the new zoo is 3
All animals in new zoo are ('monkey', 'dolphin', ('wolf', 'elephant', 'penguin'))
Animals brought from old zoo are ('wolf', 'elephant', 'penguin')
Last animal brought from old zoo is penguin

**using tuple output...**

```python
#!/usr/bin/python
# Filename: print_tuple.py

age = 22
name = 'Swaroop'

print '%s is %d years old' % (name, age)
print 'Why is %s playing with that python?' % name
```

## 5.3 Dict

```python
#!/usr/bin/python
# Filename: using_dict.py

# 'ab' is short for 'a'ddress'b'ook

ab = {       'Swaroop'   : 'swaroopch@byteofpython.info',
             'Larry'     : 'larry@wall.org',
             'Matsumoto' : 'matz@ruby-lang.org',
             'Spammer'   : 'spammer@hotmail.com'
     }

print "Swaroop's address is %s" % ab['Swaroop']

# Adding a key/value pair
ab['Guido'] = 'guido@python.org'

# Deleting a key/value pair
del ab['Spammer']

print '\nThere are %d contacts in the address-book\n' % len(ab)
for name, address in ab.items():
    print 'Contact %s at %s' % (name, address)

if 'Guido' in ab: # OR ab.has_key('Guido')
    print "\nGuido's address is %s" % ab['Guido']
```

## 5.4 Seq

List、Tuple、Str 都是 Seq，但是序列是什么，它们为什么如此特别呢？序列的两个主要特点是索引操作符和切片操作符。

```python
#!/usr/bin/python
# Filename: seq.py

# Slicing on a string
name = 'swaroop'
print 'characters 1 to 3 is', name[1:3]
print 'characters 2 to end is', name[2:]
print 'characters 1 to -1 is', name[1:-1]
print 'characters start to end is', name[:]

// mystr = name[:] # make a copy by doing a full slice, deep copy
```

# 6. I/O

## 6.1 File

```python
#!/usr/bin/python
# Filename: using_file.py

poem = '''\
Programming is fun
When the work is done
if you wanna make your work also fun:
        use Python!
'''

f = file('poem.txt', 'w') # open for 'w'riting
f.write(poem) # write text to file
f.close() # close the file

f = file('poem.txt')
# if no mode is specified, 'r'ead mode is assumed by default
while True:
    line = f.readline()
    if len(line) == 0: # Zero length indicates EOF
        break
    print line,
    # Notice comma to avoid automatic newline added by Python
f.close() # close the file
```

## 6.2 储存与取储存

Object to FIle 的 W/R

```python
#!/usr/bin/python
# Filename: pickling.py
## import..as语法。这是一种便利方法，以便于我们可以使用更短的模块名称

import cPickle as p
#import pickle as p

shoplistfile = 'shoplist.data'
# the name of the file where we will store the object

shoplist = ['apple', 'mango', 'carrot']

# Write to the file
f = file(shoplistfile, 'w')
p.dump(shoplist, f) # dump the object to a file
f.close()

del shoplist # remove the shoplist

# Read back from the storage
f = file(shoplistfile)
storedlist = p.load(f)
print storedlist
```

**输出**

$ python pickling.py
['apple', 'mango', 'carrot']

# 7. Exception

## 7.1 try...except

```python
#!/usr/bin/python
# Filename: try_except.py

import sys

try:
    s = raw_input('Enter something --> ')
except EOFError:
    print '\nWhy did you do an EOF on me?'
    sys.exit() # exit the program
except:
    print '\nSome error/exception occurred.'
    # here, we are not exiting the program

print 'Done'
```

## 7.2 try...finally

无论异常发生与否的情况下都关闭文件

```python
#!/usr/bin/python
# Filename: finally.py

import time

try:
    f = file('poem.txt')
    while True: # our usual file-reading idiom
        line = f.readline()
        if len(line) == 0:
            break
        time.sleep(2)
        print line,
finally:
    f.close()
    print 'Cleaning up...closed the file'
```

# 8. Python more

## 8.1 list_comprehension

```python
#!/usr/bin/python
# Filename: list_comprehension.py

listone = [2, 3, 4]
listtwo = [2*i for i in listone if i > 2]
print listtwo
```

## 8.2 function 接收 tuple和list

```python
#!/usr/bin/python
# Filename: powersum.py

def powersum(power, *args):
    '''Return the sum of each argument raised to specified power.'''
    total = 0
    for i in args:
        total += pow(i, power)
    return total

print powersum(2, 3, 4)
print
print powersum(2, 10)
```

由于在args变量前有*前缀，所有多余的函数参数都会作为一个元组存储在args中。如果使用的是**前缀，多余的参数则会被认为是一个字典的键/值对。

## 8.3 lambda表达式

lambda 语句被用来创建新的函数对象，并且在运行时返回它们。

```python
#!/usr/bin/python
# Filename: lambda.py

def make_repeater(n):
    return lambda s: s*n

twice = make_repeater(2)

print twice('word')
print twice(5)
```

wordword
10

## 8.4 exec和eval语句

exec语句用来执行储存在字符串或文件中的Python语句。例如，我们可以在运行时生成一个包含Python代码的字符串，然后使用exec语句执行这些语句。下面是一个简单的例子。

```python
>>> exec 'print "Hello World"'
Hello World
```

eval语句用来计算存储在字符串中的有效Python表达式。下面是一个简单的例子。

```python
>>> eval('2*3')
6
```

## 8.5 assert语句

assert语句用来声明某个条件是真的。例如，如果你非常确信某个你使用的列表中至少有一个元素，而你想要检验这一点，并且在它非真的时候引发一个错误，那么assert语句是应用在这种情形下的理想语句。当assert语句失败的时候，会引发一个AssertionError。

```python
>>> mylist = ['item']
>>> assert len(mylist) >= 1
>>> mylist.pop()
'item'
>>> assert len(mylist) >= 1
Traceback (most recent call last):
  File "<stdin>", line 1, in ?
AssertionError
```

## 8.6 repr函数

repr函数用来取得对象的规范字符串表示。反引号（也称转换符）可以完成相同的功能。注意，在大多数时候有eval(repr(object)) == object。

```python
>>> i = [] // i = list()
>>> i.append('item')
>>> i
['item']
>>> `i`
"['item']"
>>> repr(i)
"['item']"
>>>
```

> 基本上，repr函数和反引号用来获取对象的可打印的表示形式。你可以通过定义类的__repr__方法来控制你的对象在被repr函数调用的时候返回的内容。


