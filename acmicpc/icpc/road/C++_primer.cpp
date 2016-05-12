《C++ Primer》 第08章 标准 IO 库
——C++的输入/输出（input/output）由标准库提供。标准库定义了一族类型，支持对文件和控制窗口等设备的读写（IO）。
第一节：面向对象的标准库
@ 学习摘录062：iostream定义读写控制窗口的类型
——istream 从流中读取
——ostream 写到流中去
——iostream 对流进行读写；从istream和ostream派生而来
@ 学习摘录063：fstream定义读写已命名文件的类型
——ifstream 从文件中读取；由istream派生而来
——ofstream 写入到文件中；由ostream派生而来
——fstream读写文件；由iostream派生而来
《C++Primer》 第09章 容器与算法
@学习摘录072：顺序容器
——将单一类型元素聚集起来成为容器，然后根据位置来存储和访问这些元素，这就是顺序容器。
——vector支持快速随机访问
——list支持快速插入/删除
——deque双端队列
@学习摘录073：顺序容器适配器
——适配器是根据原始的容器类型所提供的操作，通过定义新的操作接口，来适应基础的容器类型。
——stack后进先出（LIFO）栈
——queue先进先出（FIFO）队列
——priority_queue有优先级管理的队列
第一节：顺序容器的定义（初始化）
@学习摘录074：新建一个容器初始化为另一个容器的副本
——vector<int> ivec;
——vector<int> ivec2(ivec); // ok, ivec is vector<int>
——将一个容器复制给另一个容器时，类型必须匹配：容器类型和元素类型都必须相同。
@学习摘录075：初始化为一段元素的副本
——系统允许通过传递一对迭代器间接实现该功能。
  使用迭代器时，不要求容器类型相同，容器内的元素类型也可以不相同，只要它们相互兼容能进行转换即可。
——list<string> slist(svec.begin(), svec.end() );
——vector<string>::iterator mid = svec.begin() + svec.size() / 2;
——deque<string> front(svec.begin(), mid); ‘ Good !左闭右开 ’
——deque<string> back(mid, svec.end() );
摘录有想075：
——利用迭代器复制相当的方便，减少了很多限制，还可以不同类型的复制，只是也要考虑到一个方面，稳定性，如果需要隐式转换的地方还是少用的好。一段段的复制挺好的。同时，注意考虑它的区间是左闭右开的区间来的。
@学习摘录076：容器内元素的类型约束 “自己定理的类类型就得注意这个问题了！”Good! Good !
——元素类型必须支持赋值运算。
——元素类型的对象必须可复制。
@学习摘录077：容器的容器
——定义容器的容器时，有一个问题必须得注意的：
——vector<vector<string> > lines; // ok:space required between cblose>
——必须用空格隔开两个相邻的>符号，以示这是两个分开的符号。
——否则，系统会认为>>是单个符号，为右移操作符，并结果导致编译时的错误。    细节决定成败。Good!
第二节：迭代器和迭代器范围
@学习摘录078：迭代器范围   表达式：　[first,last)。
@学习摘录079：使用左闭合区间的编程意义
——左闭合区间有两个方便使用的性质，得记住：
——1.当first与last相等时，迭代器范围为空；
——2.当first与last不相等时，迭代器范围内至少有一个元素，而first指向该区间的第一个元素。
第三节：顺序容器的操作
——每种顺序容器都提供了一组有用的类型定义以及以下操作：
——1.在容器中添加元素, 删除元素
——3.设置容器的大小
——4.（如果有的话）获取容器内的第一个和最后一个元素。
@学习摘录080：在顺序容器中添加元素
——在容器中添加元素时，系统是将元素值复制到容器里。
@学习摘录081：在容器中的指定位置添加元素
——s.insert(迭代器,插入的东西);新元素是插入在迭代器指向的位置之前。返回指向新添加元素的迭代器。
——s.insert(iter,element); // insert element just before iter
摘录有想081：
——考虑到有一种特例：push_back和push_front可以相当于iter为s.begin()和s.end()时。
@学习摘录082：插入一段元素 哦 ！Good !
——看代码后，你懂的！迭代器插入位置加上迭代器的前后位置构成的左闭合区间。
——stringsarray[4] = {“quasi”, “samba”, “frollo”, “scar”};
——slist.insert(slist.end(),sarray, sarray + 4);
@学习摘录083：关系操作符（比较大小）
——/*
—— ivec1:1 3 5 7 9 12
—— ivec2:0 2 4 6 8 10 12
—— ivec3:1 3 9
—— ivec4:1 3 5 7
—— ivec5:1 3 5 7 9 12
——*/
——//ivec2 < ivec1 // true
——ivec1 < ivec3 // true
——ivec1 < ivec4 // false
——ivec1 == ivec5 // true; each element equal and same number of elements
摘录有想083：
——很明显，在容器中，比较大小；
——１.逐位对比，先比大小。　（大者为大）
——２.再比容器长度。（长者为大）
@学习摘录084：删除容器内所有的元素
——slist.clear();
——slist.erase(slist.begin(),slist.end() );
摘录有想084：
——要删除容器内所有的元素，可以调用clear函数，或将begin和end迭代器传递给erase函数。
@学习摘录085：容器中的赋值操作符
——赋值操作符首先删除其左操作数容器中的所有元素；
——然后将右操作数容器的所有元素插入到左边容器中；
——赋值后，左右两边的容器相等；
——赋值前可能两个容器长度不相等，但赋值后两个容器都具有右操作数的长度。
——c1 = c2; // replace contents of c1 with a copy of elements in c2
——c1.erase(c1.begin(),c1.end() ); // delete all elements in c1
——c1.insert(c1.begin(),c2.begin(), c2.end() ); // insert c2
@学习摘录086：重设容器--------------2013-10-03 21:11----------------------
——c.assign(b,e)//重新设置c的元素：将迭代器b和e标记的范围内所有的元素复制到c中。b和e必须不是指向c中元素的迭代器。
——c.assign(n,t)//将容器c重新设置为存储n个值为t的元素
——//followed by slist1.insert(slist1.begin(), 10, “Hiya!”);
——slist1.assign(10,“Hiya!”); // 10 elements; each one is Hiya!
——执行了上述语句后，容器slist1有10个元素，每个元素的值都是Hiya!
摘录有想086：
——assign操作跟赋值操作符的操作原理差不多，都是先清空一个容器，然后再对已清空的容器进行插入操作。
@学习摘录087：交换容器
——swap操作实现交换两个容器内所有元素的功能。
——vector<string>svec1(10); // vector with 10 elements
——vector<string>svec2(24); // vector with 24 elements
——svec1.swap(svec2);
——执行swap后，容器svec1中存储24个string类型的元素，而svec2则存储10个元素。
——关于swap的一个重要问题：
——1.该操作不会删除或插入任何元素；
——2.保证在常量时间内实现交换。
——3.由于容器内没有移动任何元素，因此迭代器不会失效。
摘录有想087：
swap 它的原理是？猜测可能是变了变量的地址，其它一切无发生改变。
第四节：vector容器的自增长
@学习摘录088：vector的增长效率
——为了使vector容器实现快速的内存分配，其实际分配的容量要比当前所需的空间多一些。
——vector容器预留了这些额外的存储区，用于存放新添加的元素。
——于是，不必为每个新元素重新分配容器。
——所分配的额外内存容量的确切数目因库的实现不同而不同。
——比起每添加一个新元素就必须重新分配一次容器，这个分配策略带来显著的效率。
——事实上，其性能非常好，因此在实际应用中，比起list和deque容器，vector的增长效率通常会更高。Good !
@学习摘录089：capacity成员
——弄清capacity（容量）与size（长度）的区别非常重要。
——size指容器当前拥有的元素个数；
——而capacity则指容器在必须分配新存储空间之前可以存储的元素总数。
——vector<int>ivec;
——for(vector<int>::size_type ix = 0; ix != 24; ++x)
—— ivec.push_back(ix);
——//size should be 24; capacity will be >= 24 and is implementationdefined
——cout<< “ivec: size: “ << ivec.size()
—— <<“ capacity: “ << ivec.capacity() << endl;
——结果：
——　ivec:size: 24 capacity: 32
@学习摘录090：选择容器
——下面列举了四种选择容器的法则。
——1.如果程序要求随机访问元素，则应使用vector或deque容器。
——2.如果程序必须在容器的中间位置插入或删除元素，则应采用list容器。
——3.如果程序不是在容器的中间位置，而是在容器首部或尾部插入或删除元素，则应采用deque容器。
——4.如果只需在读取输入时在容器的中间位置插入元素，然后需要随机记问元素，则可考虑在输入时将元素读入到一个list容器，接着对此容器重新排序，使其适合顺序访问，然后将排序后的list容器复制到一个vector容器。
第六节：string类型
@学习摘录091：string类型的查找操作
——几乎所有的查找操作，返回的是string::size_type类型的值，以下标形式标记查找匹配所发生的位置；
——当查找没有匹配值，将返回名为string::npos的特殊值。
@学习摘录092：string类型的定义（一少部分）
——s.find(args)在s中查找args的第一次出现
——s.find_first_of(args)在s中查找args的任意字符的第一次出现
——s.find_last_of(args)在s中查找args的任意字符的最后一次出现
——s.find_first_not_of(args)在s中查找第一个不属于args的字符
——s.find_last_not_of(args)在s中查找最后一个不属于args的字符
摘录有想092：
——这几个定义，看上去，个人将其归纳两点：
——1.find操作可以分为第一次出现的关键字和最后次出现的关键字进行查找的标准。
——2.find操作可以分为查找属于args的字符和不属于args的字符。
《C++ Primer》 第10章 关联容器
——关联容器通过键（key）存储和读取元素；顺序容器则通过元素在容器中的位置顺序存储和访问元素。
——1.  map　关联数组；元素通过键来存储和读取  3. multimap 不支持下标访问
——2.  set  大小可变的集合，支持通过键实现的快速读取  4. multiset
第一节：pair类型——#include <utility>
@学习摘录098：pair的创建与初始化
——pair<string, int> elem(“OK”, 110);  // holds a string and an int
—— elem.first 是 OK ， elem.second 是 110
@学习摘录100：生成新的pair对象(make_pair)
——pair<string, string> next_auth;  next_auth = make_pair(first, last); 
—默认情况下标准库使用键类型定义 < 操作符来实现键的比较。
—所用的比较函数必须在键类型上定义严格弱排序（strict weak ordering）
—这也就是说，map<first, second> 中的first类型必须支持 < （小于）操作符。
—map容器与顺序容器不同的一点是，用下标访问有可能会使map容器添加一个新的元素。 Good !
@学习摘录105：map中常用的insert操作
——m.insert(make_pair(“ok”, 12));
——m.insert(map<string, int>::value_type(“ok”, 1));
——m.inert(map<string, int>:: iterator a = m.begin(), map<string, int>::iterator b=++a);
——这三种操作，返回的都是void类型。
@学习摘录106：查找map中的元素
——m.find(k); 返回迭代器，存在，返回符合位置的迭代器，不存在，返回超出末端迭代器。
——m.count(k); 返回m中k的出现次数。根据map的性质，只能回返回0或1.
 [C/C++] 第12章 类 primer
类 : 在C++中，用类来定义自己的抽象数据类型（abstract data type）。
             —类背后蕴涵的基本思想是：数据抽象和封装
——数据抽象是一种依赖于接口和实现分离的编程（和设计）技术。
——类设计者必须关心类是如何实现的，但使用该类的程序员不必了解这些细节。
  单形参构造函数应该为explicit. 
第五节：友元
——友元（friend）机制允许一个类将其非公有成员的访问权授予指定的函数或类。
——通常，将友元声明成组地放在类定义的开始或结尾是个好主意。
——当我们在类的外部定义static成员时，无须重复指定static保留字，该保留字只出现在类定义体内部的声明处。
——static成员是类的组成部分但不是任何对象的组成部分，因此，static成员函数没有this指针。
《C++ Primer》 第13章 复制控制
 复制构造函数，赋值操作符和析构函数总称为复制控制。
 只有不存在其他构造函数时才会合成默认构造函数。
 即使我们定义了其他构造函数，也会合成复制构造函数。
 禁止复制 -- 类必须显式声明其复制构造函数为private
  当声明一个(private)复制函数但不对其定义时，可以连友元和成员中的复制也禁止。
 三法则 ： 如果类需要析构函数，则它也需要赋值操作符和复制构造函数。
 与复制构造函数或赋值操作符不同，编译器总会为我们合成一个析构函数。
 还有即使我们编写了自己的析构函数，合成析构函数仍然运行。
[C/C++] 第15章：面向对象编程 《 C++ Primer 》
 *面向对象编程基于三个基本概念：数据抽象，继承，动态绑定。
 * 一旦函数在基类中声明为虚函数，它就“一直”为虚函数。
 * 要触发动态绑定，必须满足两个条件：
      (1) 要将成员函数指定为虚函数。（默认的成员函数都是非虚函数）
      (2) 要通过基类类型的引用或指针进行函数调用。
 @摘录177：合成的派生类默认构造函数 : 基类部分由基类的默认构函数初始化。
 @摘录182：定义派生类复制构造函数
  ——如果派生类显式定义自己的复制构造函数或赋值操作符，则该定义将完全覆盖默认定义。
  ——被继承类的复制构造函数和赋值操作符负责对基类成分以及类自己的成员进行复制或赋值。
  ——派生类对象在赋值给基类对象时会被“切掉”
  ——如果析构函数为虚函，那么通过指针调用时，运行哪个析构函数将因指针所指对象类型的不同而不同。








