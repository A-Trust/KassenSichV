The native library (libasigntse.dll) should be either in classpath or set the path to the library via jna.library.path environment variable.

Please ensure that the version of the native library matches the one of your JVM (32/64Bit).  

Make sure your provided A-Trust TSE configuration file (asigntseonline.conf) is in the same folder as the native library. 

Start the programm with: 
>java -cp . -jar examples.jar
