重构HttpContent中初始化mimeMapping的工作

通过解析config/web.xml文件将所有资源文件后缀名与对应的Content-type值存入HttpmimeMapping的静态属性
mimeMapping这个Map.这样一来,服务端就支持了所有类型.

实现:
重构HttpContent的方法:initmimeMapping
使用DOM4j读取config/web.xml文件
将根标签下所有<mime-mapping>读取到,并将其中的子标签:
    <extension>中间的文本作为key
    <mime-type>中间的文本作为value

    存入mimeMapping这Map完成初始化.初始化后mimeMapping应该有1010个元素