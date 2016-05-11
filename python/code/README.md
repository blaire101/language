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
>>> print r'C:\nowhere''\\'    # 解决最后一个字符是 '\' 的情况！ 完美解决  
C:\nowhere\  
>>> print u'hello, world'      # Unicode 字符串， 在python3.0中，所有的字符串都是 Unicode 字符串  
hello, world  
>>>
```

## python helloworld.py

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
➜  code git:(master) ✗ chmod a+x helloworld.py
➜  code git:(master) ✗ ./helloworld.py
Hello World
➜  code git:(master) ✗
```

## python help()

```python
>>> help()

Welcome to Python 2.7!  This is the online help utility.

If this is your first time using Python, you should definitely check out
the tutorial on the Internet at http://docs.python.org/2.7/tutorial/.

Enter the name of any module, keyword, or topic to get help on writing
Python programs and using Python modules.  To quit this help utility and
return to the interpreter, just type "quit".

To get a list of available modules, keywords, or topics, type "modules",
"keywords", or "topics".  Each module also comes with a one-line summary
of what it does; to list the modules whose summaries contain a given word
such as "spam", type "modules spam".

help> input
Help on built-in function input in module __builtin__:

input(...)
    input([prompt]) -> value

        Equivalent to eval(raw_input(prompt)).
        (END)
```


## to python_string

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
>>> print repr("Hello, world")  
'Hello, world'  
>>> print repr(10000L)  
10000L  
>>> print str("Hello world")  
Hello world  
>>> print str(10000L)  
10000  
>>>
```

## input 与 raw_input 的区别

```
>>> raw_input("shuru : ")  
shuru : 6  
'6'  
>>> input("shuru : ")  #默认为合法的python表达式  
shuru : 5  
5  
>>>   
```

# Python Basic

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

## Operators and Expression

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

## python 控制流

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

# function

## local var

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

```Output
$ python func_local.py
x is 50
Changed local x to 2
x is still 50
```

## global var

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

```output
$ python func_global.py
x is 50
Changed global x to 2
Value of x is 2
```

## func_key

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

```output
$ python func_key.py
a is 3 and b is 7 and c is 10
a is 25 and b is 5 and c is 24
a is 100 and b is 5 and c is 50
```

## DocStrings

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
