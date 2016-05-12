vector
template < class T, class Alloc = allocator<T> > class vector; // generic template
----------------
Capacity:
vector::resize
#include <iostream>
#include <vector>
int main () {
    vector<int> myvector;
    // set some initial content:
    for (int i = 1;i < 10; i++) myvector.push_back(i);
    myvector.resize(5);
    myvector.resize(8,100);
    myvector.resize(12);
    cout << "myvector contains:";
    for (int i = 0; i < myvector.size(); i++)
        cout << ' ' << myvector[i];
    cout << '\n';
  return 0;
}
/* 
Output:
myvector contains: 1 2 3 4 5 100 100 100 0 0 0 0
*/
vector::size
vector::capacity
vector::reserve
#include <iostream>
#include <vector>
int main () {
    vector<int>::size_type sz;
    vector<int> foo;
    sz = foo.capacity();
    cout << "making foo grow:\n";
    for (int i = 0; i < 100; ++i) {
        foo.push_back(i);
        if (sz != foo.capacity()) {
            sz = foo.capacity();
            cout << "capacity changed: " << sz << '\n';
        }
    }
    vector<int> bar;
    sz = bar.capacity();
    bar.reserve(100);   // this is the only difference with foo above
    cout << "making bar grow:\n";
    for (int i = 0; i < 100; ++i) {
        bar.push_back(i);
        if (sz != bar.capacity()) {
            sz = bar.capacity();
            cout << "capacity changed: " << sz << '\n';
        }
    }
    return 0;
}

vector::insert
// inserting into a vector
#include <iostream>
#include <vector>
using namespace std;
int main () {
    vector<int> myvector(3, 100);
    vector<int>::iterator it;
    it = myvector.begin();
    it = myvector.insert(it , 200 );
    myvector.insert (it, 2, 300);
    // "it" no longer valid, get a new one:
    it = myvector.begin();
    vector<int> anothervector (2, 400);
    myvector.insert (it+2, anothervector.begin(), anothervector.end());
    int myarray[] = { 501, 502, 503 };
    myvector.insert (myvector.begin(), myarray, myarray+3);
    cout << "myvector contains:";
    for(it = myvector.begin(); it < myvector.end(); it++) {
        cout << ' ' << *it;
    }
    cout << '\n';
    return 0;
}
/**
 Output:
 myvector contains: 501 502 503 300 300 400 400 200 100 100 100
**/
----Element access:----
operator[]   at   front   back
----Modifiers:----
vector::assign   :  Assigns new contents to the vector, replacing its current contents, and modifying its size accordingly.
push_back  pop_back  insert  erase  swap  clear

--list--
splice
remove
unique
merge
sort
reverse


