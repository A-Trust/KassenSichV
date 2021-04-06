import argparse
import time
from sys import exit

import tse_bindings
from tse_bindings import AUTH_OK, EXECUTION_OK, NOT_INITIALIZED

FAILURE = -1

CLIENT_ID = "pythonExample"
PROCESS_DATA = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar"
PROCESS_TYPE = "Kassenbeleg"
USER_ID = "admin"

parser = argparse.ArgumentParser()
parser.add_argument("--logging", action="store_true", help="Enable logging.")
args = parser.parse_args()


def initialize_tse(
    pin,    # type: str
    puk     # type: str
):          # type: (...) -> int
    result = tse.at_set_pins(pin, puk)
    if not result == EXECUTION_OK:
        print("at_set_pins failed: %d" % result)

        return result

    result, auth_result, remaining_retries = tse.authenticate_user(
        USER_ID, pin)
    if not result == EXECUTION_OK:
        print("authenticate_user failed: %d"
              " \tauth_result: %d \tremaining_retries: %d" % (
                  result, auth_result, remaining_retries))

        return result

    if not auth_result == AUTH_OK:
        print("auth_result not ok: %d" % auth_result)

        return result

    result = tse.initialize_description_set()
    if not result == EXECUTION_OK:
        print("initialize_description_set failed: %d" % result)

        return result

    result = tse.log_out(USER_ID)
    if not result == EXECUTION_OK:
        print("log_out failed: %d" % result)

        return result

    print("TSE initialization successful.\n")

    return EXECUTION_OK


def register_client_id(
    pin,    # type: str
):          # type: (...) -> int
    result, auth_result, remaining_retries = tse.authenticate_user(
        USER_ID, pin)
    if not result == EXECUTION_OK:
        print("authenticate_user failed: %d"
              " \tauth_result: %d \tremaining_retries: %d" % (
                  result, auth_result, remaining_retries))

        return result

    if not auth_result == AUTH_OK:
        print("auth_result not ok: %d" % auth_result)

        return result

    result = tse.at_register_client_id(CLIENT_ID)
    if not result == EXECUTION_OK:
        print("at_register_client_id failed: %d" % result)

        return result

    result = tse.log_out(USER_ID)
    if not result == EXECUTION_OK:
        print("log_out failed: %d" % result)

        return result

    return EXECUTION_OK


def write_file(
    filename,    # type: str
    export_data  # type: bytes
):               # type: (...) -> None
    with open(filename, "wb") as f:
        f.write(export_data)


if __name__ == "__main__":

    tse = tse_bindings.Tse(CLIENT_ID)

    pin = "geheimpin"
    puk = "geheimpuk"

    # to enable logging run:   example.py --logging
    if args.logging:
        result = tse.cfg_set_logging_enabled(True)
        if not result == EXECUTION_OK:
            print("cfg_set_logging_enabled failed: %d" % result)

    result = tse.at_load()
    if not result == EXECUTION_OK:
        print("at_load failed: %d" % result)
        exit(FAILURE)

    result, version = tse.at_get_version()
    if not result == EXECUTION_OK:
        print("at_get_version failed: %d" % result)
    else:
        print("\na.sign TSE v%s\n" % version)

    lifecycle_state = 0
    result, lifecycle_state = tse.at_get_lifecycle_state()
    if not result == EXECUTION_OK:
        print("at_get_lifecycle_state failed: %d" % result)

    # TSE initialization if necessary
    if lifecycle_state == NOT_INITIALIZED:
        if not initialize_tse(pin, puk) == EXECUTION_OK:
            print("TSE initialization failed.")
            print("at_unload returns: %d" % tse.at_unload())
            exit(FAILURE)

    # Client id registration if necessary
    if not register_client_id(pin) == EXECUTION_OK:
        print("Client id registration failed.")

    # Make transaction
    result = tse.start_transaction("", "")
    if not result == EXECUTION_OK:
        print("start_transaction failed: %d" % result)
    else:
        log_time_str = time.strftime(
            "%y-%m-%dT%H:%M:%S" + 'Z', time.gmtime(tse.last_log_time))
        print("start_transaction\n"
              " log_time: %s\n"
              " transaction_number: %d\n"
              " signature_counter: %d" % (
                  log_time_str,
                  tse.transaction_number,
                  tse.signature_counter))

    result = tse.finish_transaction(PROCESS_TYPE, PROCESS_DATA)
    if not result == EXECUTION_OK:
        print("finish_transaction failed: %d" % result)
    else:
        log_time_str = time.strftime(
            "%y-%m-%dT%H:%M:%S" + 'Z', time.gmtime(tse.last_log_time))
        print("\nfinish_transaction\n"
              " log_time: %s\n"
              " transaction_number: %d\n"
              " signature_counter: %d" % (
                  log_time_str,
                  tse.transaction_number,
                  tse.signature_counter))

    # Export data
    maximum_number_records = 0

    result, data = tse.export_data(maximum_number_records)
    if not result == EXECUTION_OK:
        print("export_data failed: %d" % result)

    write_file("export_data.tar", data)

    result = tse.at_unload()
    if not result == EXECUTION_OK:
        print("at_unload failed: %d" % result)
