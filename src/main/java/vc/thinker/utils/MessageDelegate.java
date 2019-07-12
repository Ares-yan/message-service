package vc.thinker.utils;
/**
 * @author YanZhuoMin
 *
 * @date 2019年7月10日
 */
public interface MessageDelegate {
	void handleMessage(String message, String channel);
}
