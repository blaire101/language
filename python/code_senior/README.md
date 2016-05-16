**Content List**

- 1.Datastruct
  - 1.1 List
  - 1.2 Tuple
  - 1.3 Dict
  - 1.4 Seq
- 2.I/O
  - 2.1 File
  - 2.2 储存与取储存
- 3.Exception
  - 3.1 try...except
  - 3.2 try...finally
- 4.Python more
  - 4.1 list_comprehension
  - 4.2 function 接收 tuple和list
  - 4.3 lambda 表达式
  - 4.4 exec和eval语句
  - 4.5 assert 语句
  - 4.6 repr 函数

# 1. Datastruct

## 1.1 List

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


## 1.2 Tuple


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

## 1.3 Dict

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

## 1.4 Seq

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

# 2. I/O

## 2.1 File

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

## 2.2 储存与取储存

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

# 3. Exception

## 3.1 try...except

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

## 3.2 try...finally

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

## 3.3 异常总结

```  
>>> raise Exception("hello") 引发异常 raise 异常类/异常实例  
>>> import exceptions  
Exception 是所有异常的基类  

@学习摘录 301：自定义异常类  
 —— class SomeCustomException(Exception) : pass  
 
@学习摘录 302：捕获异常  
 try :  
     x = input("x : ")  
     y = input('y : ')   
     print x / y  
 except ZeroDivisionError :  
     print "The second num can't zero!"  
 except TypeError :  
     print "That wasn't a number."  
@学习摘录 303：用一个块捕捉两个异常  
 try :  
     x = input("x : ")  
     y = input('y : ')   
     print x / y  
 except (ZeroDivisionError, TypeError), e :  
     print e  
 except : 这样子写的话，就是捕捉所有异常了，不推荐！  
@学习摘录 304：异常上浮-主程序-堆栈跟踪  
     try  
     except :  
     else :  
     finally : 最后  
```

# 4. Python more

## 4.1 list_comprehension

```python
#!/usr/bin/python
# Filename: list_comprehension.py

listone = [2, 3, 4]
listtwo = [2*i for i in listone if i > 2]
print listtwo
```

## 4.2 function 接收 tuple和list

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

## 4.3 lambda表达式

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

## 4.4 pass,exec,eval语句

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

## 4.5 assert语句

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

## 4.6 repr函数

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

