package serialization;

public class FinStream extends ControlFrame{
	private int streamID;
	private int statusCode;
	
	
	
	public FinStream(int streamID){
		setStreamID(streamID);
		
	}
	/**
	 * Set the value of streamID in the frame
	 * @param streamID
	 */
	public void setStreamID(int streamID){
		this.streamID = streamID;
	}
	/**
	 * Gets the StreamId
	 */
	public int getStreamID(){
		return streamID;
	}
	
	
	/**
	 * Set the value of streamID in the frame
	 * @param streamID
	 */
	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}
	/**
	 * Gets the StatusCode
	 */
	public int getStatusCode(){
		return statusCode;
	}

}
