# Changelog
All notable changes to this project will be documented in this file.

Note that from version 1.0.1.0 the installer version takes the form x.y.z.w where x.y.0 is the SMAERS version number, and z.w.0 is the asigntse.dll version number.

## [1.2.1.2] - 2021-05-06
- new function at_getMaxLicencedClients
- SMAERS fixes for audit logging and minimum PIN/PUK length
- update service fix to handle LAN TSE
- installer/setup fixes

## [1.0.1.0] - 2021-04-02
Initial release version of asigntse.dll.
- various bug fixes

## [1.0.0] - 2021-03-24
Initial SMAERS release version

## [0.11.0] - 2021-03-19
### Added
### Changed 
- Separation of TSE and SMAERS
- SMAERS services
- TPM no longer used for storage

## [0.9.9_2] - 2020-12-03
### Added
### Changed 
- More logging during TPM installation
- Updates to installer

## [0.9.9_1] - 2020-23-10
### Added
- Add ERROR_SE_ALREADY_INITIALIZED = -5058.

### Changed 
- Update initializeDescriptionSet.
- Update initializeDescriptionNotSet.
- TSE is not initialized anymore in partner account.
- Examples are distributed in a separate archive

## [0.9.9] - 2020-10-13
### Added
- Add cfgSetHttpProxyWithUsernameAndPassword.
- Add cfgSetRetries.
- Add cfgSetTimeout.
- Add at_registerClientId.
- Add ERROR_AT_LOAD_NOT_CALLED = -3018.
- Add ERROR_STORAGE_FAILURE = -5002.
- Add ERROR_TPM_CONNECT = -5050.
- Add ERROR_INVALID_CLIENT_ID = -5051.
- Add ERROR_CLIENT_ID_NOT_REGISTERED = -5052.
- Add ERROR_CLIENT_ID_REGISTRATION_FAILED = -5053.
- Add ERROR_CANNOT_RETRIEVE_REGISTERED_CLIENT_IDS = -5054.
- Add ERROR_CORRUPTED_REGISTERED_CLIENT_IDS = -5055.
- Add ERROR_CORRUPTED_APP_DATA = -5056.
- Add ERROR_SET_PINS_FAILED = -5057.

### Changed
- Update C example.
- Update C# example.
- Update Java example.
- Update Python example.
- Enforce at_load.
- TPM is used.
- Setup is required.

## [0.9.8] - 2020-04-03
### Added
### Changed
- Fix a bug regarding cfgSetHttpProxy.

## [0.9.7] - 2020-03-16
### Added
- Add cfg function family.
- Add A-Trust specific functions, extending the seapi functionality.
- Add Logging.
- Add Windows stdcall build.
 
### Changed
- Rename asigntse_free to at_free.
  
## [0.9.6] - 2019-01-20
### Added
### Changed
- Bug fixes.

## [0.9.5] - 2020-01-15
### Added
### Changed
- Bug fixes.
  
## [0.9.4] - 2020-01-03
### Added
- Add withTse version of seapi functions.

### Changed
- Bug fixes.

## [0.9.3] - 2019-10-29
### Added

### Changed 
- Set explicit calling convention in C# example.

## [0.9.2] - 2019-10-25
### Added
- Java GUI example.
- Java QR code example.

### Changed 
- New asigntseonline.conf format.
  - url renamed to conn_param=https://hs-abnahme.a-trust.at/asigntseonline/v1
  - api_key renamed to atrust_api_key
  - section [xyz] moved to atrust_vtss_id=xyz
  - section is now a friendly used for WithTSE postfix 
  - if more than one section is present, [default] section is mandatory
  - tss_type=1 added for future use
- Added asigntse.h.
- Changed c types to stdint fixed length types.
- Renamed se_free to asigntse_free.
- Added function variants with the WithTse postfix, to select different tses.
- Changed tm time structs to unix timestamps in all functions.
- Update c# example.
- Update java example.
- Update c example.
- Bug fixes. 


## [0.9.1] - 2019-09-05
### Added
- C# code example.
- Shared libraries for linux i686 and x86_64.
- DLL for win64.

### Changed
- New asigntseonline.conf format.
- All binary output data is now raw binary instead of base64/hex encoded.
- Fix C code example.
- Fix possible null pointer exceptions in the java bindings.
- Fix a resource leak in the windows dlls. 

## [0.9.0] - 2019-08-22
### Added
- DLL for win32.
- C and Java code examples.
