速度更快，功能更强大的bean拷贝工具
<ol>
	<li>支持pojo和map之间的互相拷贝；</li>
 	<li>支持pojo和pojo之间的互相拷贝；</li>
	<li>支持枚举和字符串间的互相拷贝，并支持自定义枚举值的识别方法；</li>
	<li>支持date、timestamp与string、long之间的转换；</li>
	<li>支持基本类型和封装类之间的转换；</li>
	<li>支持数值型之间以及数值型与string之间的转换；</li>
	<li>支持数组的转换；</li>
	<li>可扩展其他自定义字段映射；</li>
	<li>单个字段无法拷贝不抛异常且不影响其他字段；</li>
	<li>基于字节码预编译实现，比较apache-commons的BeanUtils性能好很多，功能上又较cglib的beanCopier工具类强；</li>
	<li>不支持嵌套类的拷贝；</li>
	<li>有单元测试，稳定性可保障；</li>
</ol>

<p>更多介绍参见：https://github.com/chenjw/beanCopyUtils/wiki</p>

=============
