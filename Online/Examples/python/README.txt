Please copy the

 - configuration file (asigntseonline.conf) from the <install-dir>\config directory
 - and the native library (asigntse.dll) from the respective (*) <install-dir>\bin directory

to the directory where tse_bindings.py and example.py are.

Then start example.py. To enable logging append the "--logging" parameter to your execution command.

(*) When using a 32 bit Version of python, please use the dll from a.sign TSE\bin\win32-cdecl\
The version of python can be determined by "py -0"