

## rx1 相关一句话总结

#### 1.创建操作
* create  
&emsp;直接通过Observable的静态方法来创建  
&emsp;通过已经创建的observable对象，再次创建，类似builder建造者模式  
* unsafeCreate  
&emsp;功能同create被deprecate掉的创建方法，可能存在背压问题
* defer  
&emsp;订阅时才创建Observable，每次订阅都会创建新的Observable
* empty/never/throw  
&emsp;测试用的，分别为直接onComplete、不发送任何事件、直接onError
&emsp;测试用的，分别为直接onComplete、不发送任何事件、直接onError
* from  
&emsp;从跌代器Iterable、数组中按顺序获取数据源  
* interval  
&emsp;订阅时开始周期性发射，可以通过延迟参数指定创建时是否立即发射
* range  
&emsp;从指定下标开始，发射指定数量的数据的源
* repeat  
&emsp;将数据源重复发送几次
* repeatWhen  
&emsp;发射完毕后（不要发射onComplete），等待n秒后，重新从头开始发射  
&emsp;`repeatWhen将源发射完毕时将此源应用一个函数，入参为此源，返回另一个源2，若源2发射的是next，则重复一次;若源2发射的是complete/error则不会重复` 
* timer  
&emsp;指定延迟后发射onNext=0，紧跟onComplete


#### 2.变换操作
* buffer  
&emsp;创建一个包裹，收集一定数量的源头数据，然后在合适时机将整包数据作为List一次性发射出去  
&emsp;如果源头发射了error，那buffer会立即传递这个error，而不会发射包裹中已缓存的数据
* groupBy  
&emsp;根据源头数据的某一些特殊来进行分组，并下游收到 分组的observable
* map  
&emsp;将源头的每一项数据 转换成 另一项 数据
* flatMap  
&emsp;将源头的每一项数据，转换成多项发射出去，两组多项之间是无序的。因其内部使用merge  
&emsp;`理解后的心得：flatMap将源头发射的数据作为入参，应用到一个函数中，此函数返回另一个源，另一个源可以发射自有的多个数据`
* concatMap  
&emsp;将源头的每一项数据，转换成多项发射出去，两组多项之间是有序的。因其内部使用concat
* switchMap  
&emsp;将源头的每一项数据，转换成多项发射出去，两组多项之间既不能说是有序、也不能说是无序的。  
&emsp;因其内部使用switchOnNext，新的observable产生时，会中断旧的observable发射
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


#### 3.过滤操作
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

  
#### 4.结合操作
* zip  
&emsp;接收2个或以上个数的Observable，将多个源头的数据按1:1:1...的方式合并成一个observable  
&emsp;它只发射与发射数据项最少的那个Observable一样多的数据  
&emsp;如果子observable既没有发射onError也没有发射onComplete，则发出的数据流会停止在数量较少的那一次onNext  
&emsp;如果组合的observable没有发射onError，则最终不会发射onError  
&emsp;如果组合的observable中有一个发射了onError，则Subscriber中的onError会立即执行，并且不会再触发onNext和onCompleted  
* merge  
&emsp;将多个observable混合输出，看起来像是只有唯一一个observable一样  
&emsp;与zip的区别是，merge不会组装子observable的数据，merge是直接将子observable的源数据直接发送出去
* concat  
&emsp;连接多个observable，并且输出有序，先完全发送完第一个子observable后，再发送第二个子observalbe，以此类推
* startWith  
&emsp;在指定源头发射之前，将startWith中的数据或源头全部发射完毕后，再发射此源头的数据
* switchOnNext  
&emsp;观察一个Observable，这个observable发射的是一组observable，而这发射的这些observable开始发射的时机各不相同。  
&emsp;每当有新的observable开发发射时，旧的observable将被废弃
* join  
&emsp;ob1和ob2各自创建自己的时间窗口，ob1每次发射的时刻必处在ob2的某一时间窗口中，若此时间窗口内ob2已有发射若干数据，则ob1会和这若干数据进行组合；若ob2未发射数据，则ob1会待。ob2每次发射时，反之类推
* combineLatest  
&emsp;合并若干个observable，每个observable发射数据时，取其它obervable最近已经发射的数据 进行组合后，再发射组合后的数据  
&emsp;若其它observable中存在至少一个还未发射任何数据，则会等待



####  5.错误处理
* onErrorReturn  
&emsp;拦截error，并将入参call方法的返回值，转换为正常的数据并发射出去，最后发射onComplete
* onErrorResumeNext  
&emsp;拦截error，并开始发射入参指定的observable
* onExceptionResumeNext  
&emsp;过滤掉exception，并开始发射入参指定的observable  
&emsp;与onErrorResumeNext的区别是：onErrorResumeNext既能过滤error，也能过滤exception；而onExceptionResumeNext只能过滤exception
* retry  
&emsp;当onError时，不会发射error，而是重试n次或规限重复，完全从源头重新开始发射。
* retryWhen  
&emsp;将onError中的Throwable传递给一个函数，这个函数产生另一个Observable，retryWhen观察它的结果再决定是不是要重新订阅原始的Observable。如果这个Observable发射了一项数据，它就重新订阅，如果这个Observable发射的是onError通知，它就将这个通知传递给观察者然后终止。


#### 辅助操作
* delay  
&emsp;对数据项和onComplete延迟指定时长后发射  
&emsp;但不会延迟onError，它会立即发射onError，并丢弃因延迟而等待的数据项和onComplete
* delay(Func1)  
&emsp;针对每一项数据源观察Func1返回的Observable，若此observable终止，它就会发射对应的数据源  
* delaySubscription  
&emsp;延迟订阅原始的observable
* do  
&emsp;注册一个动作，用来观察原始Observable生命周期事件  
&emsp;doOnEach、doOnNext、doOnError、doOnCompleted、
&emsp;doOnSubscribe、doOnUnsubscribe、doOnTerminate、finallyDo


#### 连接操作
* Connect  
&emsp;可连接的Observable不会在订阅时就开始发射数据，而是直到使用了Connect操作符时才会开始。用这个方法，可以实现等待所有的观察者都订阅了Observable之后再开始发射数据
*

#### *Single*
    轻量级的Obsevable，但其回调方法不是onComplete()/onNext()/onError()，而是onSuccess()/onError()。

#### *Subject*

    Subject 是 Observable 的一个扩展，同时还实现了 Observer 接口。
    它可以像 Observer 一样接收事件，同时还可以像 Observable 一样把接收到的事件再发射出去。  

* PublishSubject  
&emsp;当一个数据发射到 PublishSubject 中时，PublishSubject 将立刻把这个数据发射到订阅到该 subject 上的所有 subscriber 中。  
&emsp;只接收PublishSubject被订阅之后发送的数据

* ReplaySubject  
&emsp;可以缓存所有发射给他的数据。当一个新的订阅者订阅的时候，缓存的所有数据都会发射给这个订阅者。

* BehaviorSubject  
&emsp;只保留最后一个值。   
&emsp;会接收到BehaviorSubject被订阅之前的最后一个数据，再接收订阅之后发射过来的数据。  
&emsp;如果BehaviorSubject被订阅之前没有发送任何数据，则会发送一个默认数据。
  
* AsyncSubject  
&emsp; 会接收AsyncSubject的onComplete()之前的最后一个数据。



# 对比

1. retryWhen、repeatWhen  
   * retryWhen以onError作为条件决定是否重复
   * repeatWhen以onComplete作为条件决定是否重复
   
2. cc













