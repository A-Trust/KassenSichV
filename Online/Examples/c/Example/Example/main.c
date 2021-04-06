#include "stdio.h"
#include "asigntse-cbindgen.h"
#include "stdlib.h"
#include "stdint.h"

#include <string.h>
#include <time.h>

#define CLIENT_ID PARAM_STR(cExample)
#define PROCESS_DATA PARAM_STR2BA(Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar)
#define PROCESS_TYPE PARAM_STR(Kassenbeleg)
#define USER_ID PARAM_STR(admin)

int initialize_tse(
	size_t pin_len, const uint8_t* pin,
	size_t puk_len, const uint8_t* puk) {
	int32_t result = 0;

	if ((result = at_setPins(
		pin, (uint32_t)pin_len, 
		puk, (uint32_t)puk_len)) != EXECUTION_OK) {
		(void)printf("at_setPins failed: %d\n", result);

		return result;
	}
	
	AuthenticationResult auth_result = 0;
	int16_t remaining_retries = 0;
	if ((result = authenticateUser(
		USER_ID,
		pin, (uint32_t)pin_len,
		&auth_result,
		&remaining_retries)) != EXECUTION_OK) {
		(void)printf("authenticateUser failed: %d", result);
		(void)printf("\tauth_result: %d \tremaining_retries: %d\n",
			auth_result, remaining_retries);

		return result;
	}

	if (auth_result != AUTHENTICATIONRESULT_AUTH_OK) {
		(void)printf("auth_result not ok: %d", auth_result);

		return result;
	}

	if ((result = initializeDescriptionSet()) != EXECUTION_OK) {
		(void)printf("initializeDescriptionSet failed: %d\n", result);

		return result;
	}

	if ((result = logOut(USER_ID)) != EXECUTION_OK) {
		(void)printf("logOut failed: %d\n", result);

		return result;
	}

	(void)printf("TSE initialization successful.\n");

	return EXECUTION_OK;
}

int register_client_id(size_t pin_len, const uint8_t* pin) {
	int32_t result = 0;

	AuthenticationResult auth_result = 0;
	int16_t remaining_retries = 0;
	if ((result = authenticateUser(
		USER_ID,
		pin, (uint32_t)pin_len,
		&auth_result,
		&remaining_retries)) != EXECUTION_OK) {
		(void)printf("authenticateUser failed: %d", result);
		(void)printf("\tauth_result: %d \tremaining_retries: %d\n",
			auth_result, remaining_retries);

		return result;
	}

	if (auth_result != AUTHENTICATIONRESULT_AUTH_OK) {
		(void)printf("auth_result not ok: %d", auth_result);

		return result;
	}

	if ((result = at_registerClientId(CLIENT_ID)) != EXECUTION_OK) {
		(void)printf("at_registerClientId failed: %d\n", result);

		return result;
	}

	if ((result = logOut(USER_ID)) != EXECUTION_OK) {
		(void)printf("logOut failed: %d\n", result);

		return result;
	}

	return EXECUTION_OK;
}

void save_file(const char* path, const uint8_t* buf, size_t size) {
	FILE* fp;

	fp = fopen(path, "wb");

	(void)fwrite(buf, size, 1, fp);
	(void)fclose(fp);
}

int main(void) {
	int32_t result = 0;

	const uint8_t pin[9] = { 'g', 'e', 'h', 'e', 'i', 'm', 'p', 'i', 'n' };
	const uint8_t puk[9] = { 'g', 'e', 'h', 'e', 'i', 'm', 'p', 'u', 'k' };

/* build DebugLogging */
#ifdef LOGGING
	if ((result = cfgSetLoggingEnabled(true)) != EXECUTION_OK) {
		(void)printf("cfgSetLoggingEnabled failed: %d\n", result);
	}
#endif

	if ((result = at_load()) != EXECUTION_OK) {
		(void)printf("at_load failed: %d\n", result);

		exit(EXIT_FAILURE);
	}
	
	unsigned char* version = NULL;
	uint32_t version_len = 0;
	if ((result = at_getVersion(&version, &version_len)) != EXECUTION_OK) {
		(void)printf("at_getVersion failed: %d\n", result);
	}
	else {
		(void)printf("a.sign TSE v%s\n\n", version);
	}
	
	LifecycleState state = 0;
	if ((result = at_getLifecycleState(&state)) != EXECUTION_OK) {
		(void)printf("at_getLifecycleState failed: %d\n", result);
	}

	/* TSE initialization if necessary */
	if (state == LIFECYCLESTATE_NOTINITIALIZED) {
		if (initialize_tse(sizeof(pin), pin, sizeof(puk), puk) != EXECUTION_OK) {
			(void)printf("TSE initialization failed.\n\n");

			goto unload;
		}
	}

	/* Client id registration */
	if (register_client_id(sizeof(pin), pin) != EXECUTION_OK) {
		(void)printf("Client id registration failed.\n\n");
	}

	/* Make transaction */
	uint32_t transaction_number = 0;
	unsigned char* serial_number = NULL;
	uint32_t serial_number_len = 0;
	uint32_t signature_counter = 0;
	unsigned char* signature_value = NULL;
	uint32_t signature_value_len = 0;
	int64_t log_time = 0;

	char buffer[80] = { 0 };
	if ((result = startTransaction(
		CLIENT_ID,
		NULL, 0,
		NULL, 0,
		NULL, 0,
		&transaction_number,
		&log_time,
		&serial_number, &serial_number_len,
		&signature_counter,
		&signature_value, &signature_value_len)) != EXECUTION_OK) {
		(void)printf("\nstartTransaction failed: %d\n", result);

		goto unload;
	}
	else {
		struct tm* time_info = gmtime(&log_time);
		(void)strftime(buffer, 80, "%FT%T%z", time_info);

		(void)printf("\nstartTransaction\nlog_time: %s\ntransaction_number:"
			" %d\nsignature_counter:  %d\n\n", buffer, transaction_number,
			signature_counter);

		at_free(&serial_number);
		at_free(&signature_value);
	}

	if ((result = finishTransaction(
		CLIENT_ID,
		transaction_number,
		PROCESS_DATA,
		PROCESS_TYPE,
		NULL, 0,
		&log_time,
		&signature_value, &signature_value_len,
		&signature_counter)) != EXECUTION_OK) {
		(void)printf("\nfinishTransaction failed: %d\n", result);
	}
	else {
		struct tm* time_info = gmtime(&log_time);
		(void)strftime(buffer, 80, "%FT%T%z", time_info);

		(void)printf("finishTransaction\nlog_time: %s\ntransaction_number:"
			" %d\nsignature_counter:  %d\n", buffer, transaction_number,
			signature_counter);

		at_free(&signature_value);
	}

	/* Export data */
	uint32_t maximum_number_records = 0;
	unsigned char* data = NULL;
	uint32_t data_len = 0;
	if ((result = exportData(
		maximum_number_records, 
		&data, &data_len)) != EXECUTION_OK) {
		(void)printf("exportData failed: %d\n", result);
	}
	else {
		save_file("./export_data.tar", data, data_len);

		at_free(&data);
	}

	unload:
	if ((result = at_unload()) != EXECUTION_OK) {
		(void)printf("at_unload failed: %d", result);

		exit(EXIT_FAILURE);
	}

	return 0;
}