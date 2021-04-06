package at.atrust.asigntse.seapi;

public class TsePinState {

/*
pub const PinStateFlags_stateInitialized : PinStateFlags	= 0x00;
pub const PinStateFlags_adminPinTransportState	: PinStateFlags	= 0x01;
pub const PinStateFlags_adminPukTransportState : PinStateFlags	= 0x02;
pub const PinStateFlags_timeAdminPinTransportState : PinStateFlags = 0x04;
pub const PinStateFlags_timeAdminPukTransportState : PinStateFlags	= 0x0;
#[doc = "Pin States:"]
#[doc = "    	stateInitialized					= 0x00"]
#[doc = "    	adminPinTransportState				= 0x01"]
#[doc = "    	adminPukTransportState				= 0x02"]
#[doc = "    	timeAdminPinTransportState			= 0x04"]
#[doc = "    	timeAdminPukTransportState			= 0x08"]
pub type PinStateFlags= u32;
*/
	

	public static final int STATE_INITIALIZED              = 0x00;
	public static final int ADMIN_PIN_TRANSPORT_STATE      = 0x01;
	public static final int ADMIN_PUK_TRANSPORT_STATE      = 0x02;
	public static final int TIME_ADMIN_PIN_TRANSPORT_STATE = 0x04;
	public static final int TIME_ADMIN_PUK_TRANSPORT_STATE = 0x08;
	
}
