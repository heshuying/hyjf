package com.hyjf.api.server.util;

/**
 * @author xiasq
 * @version ServiceException, v0.1 2017/11/30 15:57
 */
public class ServiceException extends RuntimeException {

	private ErrorCode errorCode;

	public ServiceException() {
	}

	public ServiceException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ServiceException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public boolean hasErrorCode() {
		return errorCode != null;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
