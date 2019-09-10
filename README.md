

## rx1 相关一句话总结

#### 创建操作
* create  
&emsp;直接通过Observable的静态方法来创建  
&emsp;通过已经创建的observable对象，再次创建，类似builder建造者模式
* defer  
&emsp;订阅时才创建Observable，每次订阅都会创建新的Observable
* empty/never/throw  
&emsp;测试用的，分别为直接onComplete、不发送任何事件、直接onError
* from  
&emsp;从跌代器Iterable、数组中按顺序获取数据源  
* interval  
&emsp;订阅时开始周期性发射，可以通过延迟参数指定创建时是否立即发射
* repeat  
&emsp;将数据源重复发送几次
* repeatWhen  
&emsp;发射完毕后（不要发射onComplete），等待n秒后，重新从头开始发射
* retry  
&emsp;当onError时，不会发射error，而是重试n次，完全从源头重新开始发射
* timer  
&emsp;指定延迟后发射onNext=0，紧跟onComplete


#### 变换操作
* buffer  
&emsp;创建一个包裹，收集一定数量的源头数据，然后在合适时机将整包数据作为List一次性发射出去  
&emsp;如果源头发射了error，那buffer会立即传递这个error，而不会发射包裹中已缓存的数据
* flatMap  
&emsp;将源头的一项数据，转换成多项发射出去，两组多项之间是无序的
* groupBy  
&emsp;根据源头数据的某一些特殊来进行分组，并下游收到 分组的observable
* map  
&emsp;将源头的一项数据 转换成 另一项 数据
* cast  
&emsp;将发射的源数据都强制转换成另一种类型。只能是父类转为子类
* encode  
&emsp;在StringObservable类中，不是标准RxJava的一部分，它也是一个特殊的map操作符
&emsp;将一个发射字符串的Observable变换为一个发射字节数组（这个字节数组按照原始字符串中的多字节字符边界划分）的Observable。
* byLine  
&emsp;同样在StringObservable类中，也不是标准RxJava的一部分，它也是一个特殊的map操作符。
&emsp;将一个发射字符串的Observable变换为一个按行发射来自原始Observable的字符串的Observable。
* scan  
&emsp;应用一个函数，将第一项源数据直接作为结果并发射；将第一项的结果(即第一项源)与第二项源应用这个函数并发射结果，第三项源又与第二项结果 再次应用这个函数并发射结果
* window  
&emsp;将源数据分解一个个的Observable窗口，而不是每次发射一项数据。与buffer的区别是 window发射的是Observable，而 buffer发射的是 数据List


#### 过滤操作
* debounce  
&emsp;仅在过了一段指定的时间还没发射后续数据时才发射一个数据  
&emsp;效果等同于throttleWithTimeout
* distinct  
&emsp;只允许还没有发射过的数据项通过  
&emsp;或者根据源数据来生成此数据对应的key，根据key是否相同来判定 是否重复 而过滤
* distinctUntilChanged  
&emsp;只判定一个数据和它的直接前驱是否是不同的。  
&emsp;distinct 和 distinctUntilChanged 默认不在任何特定的调度器上执行。
* elementAt  
&emsp;发射源序列中指定位置的数据，索引从0开始。  
&emsp;如果你传递的是一个负数，或者原始Observable的数据项数小于index+1，将会抛出一个IndexOutOfBoundsException异常
* elementAtOrDefault  
&emsp;如果索引值大于数据项数，它会发射一个默认值（通过额外的参数指定），而不是抛出异常。  
&emsp;但是如果你传递一个负数索引值，它仍然会抛出一个IndexOutOfBoundsException异常。
* filter  
&emsp;过滤源头，filter函数返回true的项将被发射出去，而返回false的项会被过滤掉
* first  
&emsp;只发射源的第一项数据，然后立即发射onComplete
* firstFunc  
&emsp;发射符合条件的第一项数据，如果没有源数据，则会发射onError
* firstOrDefault  
&emsp;发射符合条件的第一项数据，如果没有源数据，则会发射默认值 及 onComplete 
* takeFirst  
&emsp;发射第一项数据，当源头无数据时，会返回一个空的Observable（不调用onNext()但是会调用onCompleted）  
&emsp;区别于first，first的源无数据时，会抛出NoSuchElementException
* single  
&emsp;发射第一项数据，并且要求源头在 onComplete之前 只能发射一项数据，若发射2项或以上则会 NoSuchElementException，并且第一项数据也不会发射出去
* ignoreElements  
&emsp;忽略所有 onNext， 但不会忽略 onError、onComplete
* last       
&emsp;类比 first
* taskLast   
&emsp;类比 takeFirst
* sample  
&emsp;发射每个采样周期内的最后一项数据
* throttleLast  
&emsp;功能 同 sample相同  
* throttleFirst 
&emsp;发射每个采样周期内的第一顶数据
* skip  
&emsp;跳过源头开始的 前N项、或 前N秒时间内 的 数据
* skipLast  
&emsp;跳过源头末尾的 后N项、或 后N秒时间内 的 数据
* take  
&emsp;只发射源头的前 N 项数据
* takeLast  
&emsp;只发射源头的后 N 项数据

  
#### 结合操作
*
*
*
*
*
*
*


#### 错误处理
*
*
*
*
*


#### 辅助操作
*
*
*
*
*
*
*


#### 条件与布尔操作
*
*
*
*
*
*
*


#### 连接操作
*
*
*
*
*
*
*




















