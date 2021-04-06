import base64
import os
import platform
import sys
from ctypes import (CDLL, POINTER, byref, c_char, c_char_p, c_int, c_int64,
                    c_uint32, c_ulong, cast, memmove)
from typing import List, Optional, Tuple


EXECUTION_OK = 0

AUTH_OK = 0
AUTH_FAILED = 1
AUTH_PINISBLOCKED = 2
AUTH_UNKNOWNUSERID = 3

UNKNOWN = 0
NOT_INITIALIZED = 1
ACTIVE = 2
SUSPENDED = 3
DISABLED = 4


class Tse:
    def __init__(
        self,
        client_id,                      # type: str
    ):                                  # type: (...) -> None
        self.transaction_number = None  # type: Optional[int]
        self.signature_value = None     # type: Optional[bytes]
        self.serial_number = None       # type: Optional[bytes]
        self.client_id = client_id      # type: str

        try:
            asigntse = CDLL("asigntse.dll")
        except OSError as e:
            if e.winerror == 193:       # type: ignore
                print("please verify you are using the correct version of the DLL:")
                print(platform.architecture()[0])
            else:
                print("Error loading dll: " + str(e))
            sys.exit(-1)

        self._asigntse = asigntse
        self._loaded = False
        self._unloaded = False
        self.last_log_time = None       # type: Optional[int]
        self.signature_counter = None   # type: Optional[int]


# SE-API Functions

# Transactions


    def start_transaction(
        self,
        process_data,    # type: str
        process_type,    # type: str
        client_id=None,  # type: Optional[str]
    ):                   # type: (...) -> int

        client_id = client_id or self.client_id

        c_transaction_number = c_int()
        log_time = c_int64()
        c_serial_number = c_char_p()
        serial_number_len = c_ulong()
        signature_counter = c_ulong()
        c_signature_value = c_char_p()
        signature_value_len = c_ulong()
        additional_data = c_char_p()

        ret = self._asigntse.startTransaction(
            c_char_p(client_id.encode()),
            len(client_id),
            c_char_p(process_data.encode()),
            len(process_data),
            c_char_p(process_type.encode()),
            len(process_type),
            additional_data,
            0,
            byref(c_transaction_number),
            byref(log_time),
            byref(c_serial_number),
            byref(serial_number_len),
            byref(signature_counter),
            byref(c_signature_value),
            byref(signature_value_len)
        )  # type: int

        self.transaction_number = c_transaction_number.value
        self.signature_counter = signature_counter.value
        self.signature_value = self._to_byte_array(
            c_signature_value, signature_value_len.value)
        self.serial_number = self._to_byte_array(
            c_serial_number, serial_number_len.value)
        self.last_log_time = log_time.value

        self._asigntse.at_free(byref(c_serial_number))
        self._asigntse.at_free(byref(c_signature_value))

        return ret

    def finish_transaction(
        self,
        process_data,             # type: str
        process_type,             # type: str
        transaction_number=None,  # type: Optional[int]
        client_id=None            # type: Optional[str]
    ):                            # type: (...) -> int

        client_id = client_id or self.client_id
        transaction_number = transaction_number or self.transaction_number

        log_time = c_int64()
        signature_counter = c_ulong()
        c_signature_value = c_char_p()
        signature_value_len = c_ulong()

        ret = self._asigntse.finishTransaction(
            c_char_p(client_id.encode()),
            len(client_id),
            transaction_number,
            c_char_p(process_data.encode()),
            len(process_data),
            c_char_p(process_type.encode()),
            len(process_type),
            None,
            0,
            byref(log_time),
            byref(c_signature_value),
            byref(signature_value_len),
            byref(signature_counter)
        )  # type: int

        self.signature_counter = signature_counter.value
        self.signature_value = self._to_byte_array(
            c_signature_value, signature_value_len.value)
        self.last_log_time = log_time.value

        self._asigntse.at_free(byref(c_signature_value))

        return ret

    def update_transaction(
        self,
        process_data,             # type: str
        process_type,             # type: str
        transaction_number=None,  # type: Optional[int]
        client_id=None            # type: Optional[str]
    ):                            # type: (...) -> int

        client_id = client_id or self.client_id
        transaction_number = transaction_number or self.transaction_number

        log_time = c_int64()
        signature_counter = c_ulong()
        c_signature_value = c_char_p()
        signature_value_len = c_ulong()

        ret = self._asigntse.updateTransaction(
            c_char_p(client_id.encode()),
            len(client_id),
            transaction_number,
            c_char_p(process_data.encode()),
            len(process_data),
            c_char_p(process_type.encode()),
            len(process_type),
            byref(log_time),
            byref(c_signature_value),
            byref(signature_value_len),
            byref(signature_counter)
        )  # type: int

        self.last_log_time = log_time.value
        self.signature_value = self._to_byte_array(
            c_signature_value, signature_value_len.value)
        self.signature_counter = signature_counter.value

        self._asigntse.at_free(byref(c_signature_value))

        return ret


# Data export Calls


    def export_data(
        self,
        maximum_number_records  # type: int
    ):                          # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportData(
            maximum_number_records,
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_data_filtered_by_transaction_number_and_client_id(
        self,
        transaction_number,  # type: int
        client_id=None       # type: str
    ):                       # type: (...) -> Tuple[int, bytes]

        client_id = client_id or self.client_id

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportDataFilteredByTransactionNumberAndClientId(
            transaction_number,
            c_char_p(client_id.encode()),
            len(client_id),
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_data_filtered_by_transaction_number(
        self,
        transaction_number  # type: int
    ):                      # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportDataFilteredByTransactionNumber(
            transaction_number,
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_data_filtered_by_transaction_number_interval(
        self,
        start_transaction_number,   # type: int
        finish_transaction_number,  # type: int
        maximum_number_records,     # type: int
    ):                              # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportDataFilteredByTransactionNumberInterval(
            start_transaction_number,
            finish_transaction_number,
            maximum_number_records,
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_data_filtered_by_transaction_number_interval_and_client_id(
        self,
        start_transaction_number,   # type: int
        finish_transaction_number,  # type: int
        maximum_number_records,     # type: int
        client_id=None,             # type: str
    ):                              # type: (...) -> Tuple[int, bytes]

        client_id = client_id or self.client_id

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportDataFilteredByTransactionNumberIntervalAndClientId(
            start_transaction_number,
            finish_transaction_number,
            c_char_p(client_id.encode()),
            len(client_id),
            maximum_number_records,
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_data_filtered_by_period_of_time(
        self,
        start_date,             # type: int
        end_date,               # type: int
        maximum_number_records  # type: int
    ):                          # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportDataFilteredByPeriodOfTime(
            c_int64(start_date),
            c_int64(end_date),
            maximum_number_records,
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_data_filtered_by_period_of_time_and_client_id(
        self,
        start_date,                 # type: int
        end_date,                   # type: int
        maximum_number_records,     # type: int
        client_id=None              # type: str
    ):                              # type: (...) -> Tuple[int, bytes]

        client_id = client_id or self.client_id

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportDataFilteredByPeriodOfTimeAndClientId(
            c_int64(start_date),
            c_int64(end_date),
            c_char_p(client_id.encode()),
            len(client_id),
            maximum_number_records,
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_certificates(self):  # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.exportCertificates(
            byref(export_data),
            byref(export_data_len)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def export_serial_numbers(self):  # type: (...) -> Tuple[int, bytes]

        serial_number = c_char_p()
        serial_number_len = c_ulong()

        ret = self._asigntse.exportSerialNumbers(
            byref(serial_number),
            byref(serial_number_len)
        )  # type: int

        data = self._to_byte_array(serial_number, serial_number_len.value)
        self._asigntse.at_free(byref(serial_number))

        return ret, data

    # restoreFromBackup is not supported

    def read_log_message(self):  # type: (...) -> Tuple[int, bytes]

        log_message = c_char_p()
        log_message_len = c_ulong()

        ret = self._asigntse.readLogMessage(
            byref(log_message),
            byref(log_message_len)
        )  # type: int

        data = self._to_byte_array(log_message, log_message_len.value)
        self._asigntse.at_free(byref(log_message))

        return ret, data


# Administrative Calls

# Time Management
# updateTime is not supported
# updateTimeWithTimeSync is not supported


# User Management


    def authenticate_user(
        self,
        user_id,  # type: str
        pin       # type: str
    ):            # type: (...) -> Tuple[int, str, int]

        authentication_result = c_int64()
        remaining_retries = c_int64()

        ret = self._asigntse.authenticateUser(
            c_char_p(user_id.encode()),
            len(user_id),
            c_char_p(pin.encode()),
            len(pin),
            byref(authentication_result),
            byref(remaining_retries),
        )  # type: int

        return ret, authentication_result.value, remaining_retries.value

    def log_out(
        self,
        user_id  # type: str
    ):           # type: (...) -> int

        ret = self._asigntse.logOut(
            c_char_p(user_id.encode()),
            len(user_id),
        )  # type: int

        return ret

    def unblock_user(
        self,
        user_id,  # type: str
        puk,      # type: str
        new_pin   # type: str
    ):            # type: (...) -> Tuple[int, str]

        unblock_result = c_int64()

        unblock_results = {
            0: 'ok',
            1: 'failed',
            2: 'pinBlocked',
            3: 'unknownUserId',
            4: 'error'
        }

        ret = self._asigntse.unblockUser(
            c_char_p(user_id.encode()),
            len(user_id),
            c_char_p(puk.encode()),
            len(puk),
            c_char_p(new_pin.encode()),
            len(new_pin),
            byref(unblock_result)
        )  # type: int

        return ret, unblock_results[unblock_result.value]


# Lifecycle
    # initialize


    def initialize_description_not_set(
        self,
        description  # type: str
    ):               # type: (...) -> int

        ret = self._asigntse.initializeDescriptionNotSet(
            c_char_p(description.encode()),
            len(description)
        )  # type: int

        return ret

    def initialize_description_set(self):  # type: (...) -> int

        ret = self._asigntse.initializeDescriptionSet()  # type: int

        return ret

    def disable_secure_element(self):  # type: (...) -> int

        ret = self._asigntse.disableSecureElement()  # type: int

        return ret

    # restoreFromBackUp is not supported

    def delete_stored_data(self):  # type: (...) -> int

        ret = self._asigntse.deleteStoredData()  # type: int

        return ret


# Statistics


    def get_max_number_of_clients(self):  # type: (...) -> Tuple[int, int]

        max_number_of_clients = c_ulong()

        ret = self._asigntse.getMaxNumberOfClients(
            byref(max_number_of_clients))  # type: int

        return ret, max_number_of_clients.value

    def get_current_number_of_clients(self):  # type: (...) -> Tuple[int, int]

        current_number_of_clients = c_ulong()

        ret = self._asigntse.getCurrentNumberOfClients(
            byref(current_number_of_clients))  # type: int

        return ret, current_number_of_clients.value

    def get_max_number_of_transactions(self):  # type: (...) -> Tuple[int, int]

        max_number_of_transactions = c_ulong()

        ret = self._asigntse.getMaxNumberOfTransactions(
            byref(max_number_of_transactions))  # type: int

        return ret, max_number_of_transactions.value

    def get_current_number_of_transactions(self):  # type: (...) -> Tuple[int, int]

        current_number_of_transactions = c_ulong()

        ret = self._asigntse.getCurrentNumberOfTransactions(
            byref(current_number_of_transactions))  # type :int

        return ret, current_number_of_transactions.value

    def get_supported_transaction_update_variants(self):  # type: (...) -> Tuple[int, str]

        supported_update_variants = c_int64()

        update_variants = {
            0: 'signedUpdate',
            1: 'unsignedUpdate',
            2: 'signedAndUnsignedUpdate'
        }

        ret = self._asigntse.getSupportedTransactionUpdateVariants(
            byref(supported_update_variants))  # type: int

        return ret, update_variants[supported_update_variants.value]


# A-Trust API Functions

# Management functions


    def at_get_version(self):  # type: (...) -> Tuple[int, Optional[str]] 

        version = c_char_p()
        version_len = c_ulong()

        ret = self._asigntse.at_getVersion(
            byref(version), byref(version_len))  # type: int

        bytes_version =  version.value

        if bytes_version:
            version_str = version.value[:version_len.value].decode()
            self._asigntse.at_free(byref(version))

            return ret, version_str
        else:
            return ret, None

    def at_get_signature_algorithm(self):  # type: (...) -> Tuple[int, Optional[str]]

        signature_algorithm = c_char_p()
        signature_algorithm_len = c_ulong()

        ret = self._asigntse.at_getSignatureAlgorithm(
            byref(signature_algorithm),
            byref(signature_algorithm_len)
        )  # type: int

        bytes_sig_alg = signature_algorithm.value

        if bytes_sig_alg:
            signature_algorithm_as_str = bytes_sig_alg[:signature_algorithm_len.value].decode(
            )
            self._asigntse.at_free(byref(signature_algorithm))

            return ret, signature_algorithm_as_str
        else:
            return ret, None

    def at_get_public_key(
        self,
        tse_id="default"  # type: str
    ):                    # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.at_getPublicKeyWithTse(
            byref(export_data),
            byref(export_data_len),
            c_char_p(tse_id.encode()),
            len(tse_id)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def at_get_open_transactions(
        self,
        tse_id="default"  # type: str
    ):      	          # type: (...) -> Tuple[int, List[c_uint32]]

        transaction_numbers = c_char_p()
        transaction_numbers_len = c_ulong()

        ret = self._asigntse.at_getOpenTransactions(
            byref(transaction_numbers),
            byref(transaction_numbers_len)
        )  # type: int

        array = cast(transaction_numbers, POINTER(c_uint32))[
            :transaction_numbers_len.value]

        return ret, array

    def at_get_signature_counter(self):  # type: (...) -> Tuple[int, int]

        signature_counter = c_ulong()

        ret = self._asigntse.at_getSignatureCounter(
            byref(signature_counter))  # type: int

        return ret, signature_counter.value

    def at_get_transaction_counter(self):  # type: (...) -> Tuple[int, int]

        transaction_counter = c_ulong()

        ret = self._asigntse.at_getTransactionCounter(
            byref(transaction_counter))  # type: int

        return ret, transaction_counter.value

    def at_get_lifecycle_state(self):  # type: (...) -> Tuple[int, int]

        lifecycle_state = c_ulong()

        ret = self._asigntse.at_getLifecycleState(
            byref(lifecycle_state))  # type: int

        return ret, lifecycle_state.value

    def at_get_serial_number(
        self,
        tse_id="default"  # type: str
    ):                    # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.at_getSerialNumberWithTse(
            byref(export_data),
            byref(export_data_len),
            c_char_p(tse_id.encode()),
            len(tse_id)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def at_suspend_secure_element(self):  # type: (...) -> int

        ret = self._asigntse.at_suspendSecureElement()  # type: int

        return ret

    def at_unsuspend_secure_element(self):  # type: (...) -> int

        ret = self._asigntse.at_unsuspendSecureElement()  # type: int

        return ret

    def at_get_certificate(
        self,
        tse_id="default"  # type: str
    ):                    # type: (...) -> Tuple[int, bytes]

        export_data = c_char_p()
        export_data_len = c_ulong()

        ret = self._asigntse.at_getCertificateWithTse(
            byref(export_data),
            byref(export_data_len),
            c_char_p(tse_id.encode()),
            len(tse_id)
        )  # type: int

        data = self._to_byte_array(export_data, export_data_len.value)
        self._asigntse.at_free(byref(export_data))

        return ret, data

    def at_get_log_time_format(self):  # type: (...) -> Tuple[int, Optional[str]]

        log_time_format = c_char_p()
        log_time_format_len = c_ulong()
        ret = self._asigntse.at_getLogTimeFormat(
            byref(log_time_format), byref(log_time_format_len)
        )  # type: int

        bytes_log_time_format = log_time_format.value

        if bytes_log_time_format:
            log_time_format_as_str = bytes_log_time_format[:log_time_format_len.value].decode(
                "utf-8")
            self._asigntse.at_free(byref(log_time_format))
            return ret, log_time_format_as_str

        return ret, None

    def at_register_client_id(
        self,
        client_id,     # type: str
    ):                 # type: (...) -> int

        ret = self._asigntse.at_registerClientId(
            c_char_p(client_id.encode()),
            len(client_id),
        )  # type: int

        return ret

    def at_set_pins(
        self,
        pin,    # type: str
        puk     # type: str
    ):          # type: (...) -> int

        ret = self._asigntse.at_setPins(
            c_char_p(pin.encode()), len(pin),
            c_char_p(puk.encode()), len(puk)
        )  # type: int

        return ret


# Startup/Shutdown


    def at_load(self):  # type: (...) -> int
        ret = self._asigntse.at_load()  # type: int
        return ret


    def at_unload(self):  # type: (...) -> int
        ret = self._asigntse.at_unload()  # type: int
        return ret



# Utility
    # at_free is being directly called in wrapper functions


# Config API Functions


    def cfg_set_config_file(
        self,
        path  # type: str
    ):        # type: (...) -> int

        ret = self._asigntse.cfgSetConfigFile(
            c_char_p(path.encode()),
            len(path)
        )  # type: int

        return ret

    def cfg_tse_add(
        self,
        tse_id,             # type: str
        tse_type,           # type: int
        conn_param,         # type: str
        atrust_tse_id,      # type: str
        atrust_api_key,     # type: str
        time_admin_id="",   # type: str
        time_admin_pwd="",  # type: str
    ):                      # type: (...) -> int

        ret = self._asigntse.cfgTseAdd(
            c_char_p(tse_id.encode()),
            len(tse_id),
            tse_type,
            c_char_p(conn_param.encode()),
            len(conn_param),
            c_char_p(atrust_tse_id.encode()),
            len(atrust_tse_id),
            c_char_p(atrust_api_key.encode()),
            len(atrust_api_key),
            c_char_p(time_admin_id.encode()),
            len(time_admin_id),
            c_char_p(time_admin_pwd.encode()),
            len(time_admin_pwd)
        )  # type: int

        return ret

    def cfg_tse_remove(
        self,
        tse_id,  # type: str
    ):           # type: (...) -> int

        ret = self._asigntse.cfgTseRemove(
            c_char_p(tse_id.encode()),
            len(tse_id)
        )  # type: int

        return ret

    def cfg_set_logging_enabled(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLoggingEnabled(enabled)  # type: int

        return ret

    def cfg_set_logging_stderr(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLoggingStderr(enabled)  # type: int

        return ret

    def cfg_set_logging_file(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLoggingFile(enabled)  # type: int

        return ret

    def cfg_set_log_dir(
        self,
        path  # type: str
    ):        # type: (...) -> int

        ret = self._asigntse.cfgSetLogDir(
            c_char_p(path.encode()),
            len(path)
        )  # type: int

        return ret

    def cfg_set_log_level(
        self,
        log_level  # type: str
    ):             # type: (...) -> int

        ret = self._asigntse.cfgSetLogLevel(
            c_char_p(log_level.encode()),
            len(log_level)
        )  # type: int

        return ret

    def cfg_set_log_append(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLogAppend(enabled)  # type: int

        return ret

    def cfg_set_log_colors(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLogColors(enabled)  # type: int

        return ret

    def cfg_set_log_details(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLogDetails(enabled)  # type: int

        return ret

    def cfg_set_log_stderr_colors(
        self,
        enabled  # type: bool
    ):           # type: (...) -> int

        ret = self._asigntse.cfgSetLogStderrColors(enabled)  # type: int

        return ret

    def cfg_set_http_proxy(
        self,
        proxy_url     # type: str
    ):                # type: (...) -> int

        ret = self._asigntse.cfgSetHttpProxy(
            c_char_p(proxy_url.encode()),
            len(proxy_url))  # type: int

        return ret

    def cfg_set_http_proxy_with_username_and_password(
        self,
        proxy_url,       # type: str
        proxy_username,  # type: str
        proxy_password,  # type: str
    ):                   # type: (...) -> int

        ret = self._asigntse.cfgSetHttpProxyWithUsernameAndPassword(
            c_char_p(proxy_url.encode()),
            len(proxy_url),
            c_char_p(proxy_username.encode()),
            len(proxy_username),
            c_char_p(proxy_password.encode()),
            len(proxy_password))  # type: int

        return ret

    def cfg_set_timeout(
        self,
        timeout,              # type: int
    ):                        # type: (...) -> int

        ret = self._asigntse.cfgSetTimeout(
            c_int64(timeout))  # type: int

        return ret

    def cfg_set_retries(
        self,
        retries,              # type: int
    ):                        # type: (...) -> int

        ret = self._asigntse.cfgSetRetries(
            c_int64(retries))  # type: int

        return ret


# Support Functions


    def _to_byte_array(
        self,
        buf,    # type: c_char_p
        length  # type: int
    ):          # type: (...) -> bytes

        data = bytearray(length)
        rprocess_typer = (c_char * length).from_buffer(data)

        memmove(rprocess_typer, buf, length)

        return data

    def get_lifecycle_string(
        self,
        state  # type: int
    ):         # type: (...) -> str

        return {
            0: "Unknown",
            1: "NotInitialized",
            2: "Active",
            3: "Suspended",
            4: "Disabled",
        }[state]

    def get_signature_value_b64(self):  # type: (...) -> bytes

        if self.signature_value:
            return base64.b64encode(self.signature_value)

        return b''
    
    def get_serial_number_b64(self):  # type: (...) -> bytes

        if self.serial_number:
            return base64.b64encode(self.serial_number)

        return b''