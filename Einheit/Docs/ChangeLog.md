ASign TSE Einheit

Version 0.9.15
29. March 2021
- Allow Cryptovision library timeout and retries to be configured (either in config file or via cfgSetTimeout() and cfgSetRetries() functions)
- Attempt auto-reconnection if Cryptovision ErrorTSETimeout (-> A-Trust error ERROR_IO) is received
- Automatic time update can handle timeout errors better
- Updated sample config file (added [config] section and removed unneccessary time_admin_name  entry)

Version 0.9.14
22. February 2021
- Log location of Cryptovision drive when opening new connection
- Graceful handling when Cryptovision DLL not found

Version 0.9.13
11. February 2021
- Logging fixes
- Write current version in log file 

Version 0.9.12
04. February 2021
- Return code mapping fixed to return correct error if config not found 
- Fix to at_unload to work properly if no default config section is present

Version 0.9.11
29. January 2021
- Fixes to functions cv_getCertificationId, cv_getApiVersion, cv_getImplementationVersion, cv_getApiVersionString, cv_getImplementationVersionString 
  which were not working when there is no default section in the config file

Version 0.9.10 
25. November 2020
- This is the first release version of the Einheit DLL.  It is only intended for use with Cryptovision.
- The library requires se-api-ex-c.dll from Cryptovision.  
- The library does not expire.