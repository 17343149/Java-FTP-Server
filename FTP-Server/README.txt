FTP附加题:
	代码为具有UI界面的代码.
	main函数里面调用了三个函数,
	show(查看某个ftp目录), upload(上传文件, 弹出UI), download(下载文件到某个目录)
	例如:
	""表示FTP根目录
	showFiles("") 表示列出FTP根目录下的文件.
	uploadFile("") 表示根据UI选择一个文件上传到FTP根目录.
	downloadFile("", "test.txt", "D://") 表示下载根目录下的test.txt文件到D://


	Notice:
		用了commons-net的jar包, 已附上.
		本人是用FTP工具创建了一个本地FTP服务器, 然后让Java程序链接上去, 实现下载与上传的功能.