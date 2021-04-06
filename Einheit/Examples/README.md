# A-Sign TSE Einheit Examples
To use these examples you will need:
- A Cryptovision TSE card
- The following files from the A-Sign TSE Einheit solution:
  - se-api-ex.dll
  - asigntse.dll
  - asigntseonline.conf

Ensure that the DLL files correspond to the solution type you are building (32bit cdecl or 64bit cdecl) and that they are in the same location as the executable.

Edit the configuration file asigntseonline.conf as required for the location of your cryptovision card and the timeAdmin PIN.

If you use the Cryptovision card with the [COM](https://github.com/A-Trust/KassenSichV/tree/main/COM/Examples) or [EXE](https://github.com/A-Trust/KassenSichV/tree/main/EXE/Examples) you should use the 32bit cdecl version of the asigntse.dll.
