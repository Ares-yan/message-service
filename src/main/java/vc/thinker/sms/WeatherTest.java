package vc.thinker.sms;
 
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import vc.thinker.utils.PureNetUtil;
 
public class WeatherTest {
	/**
	 * 获取SOAP的请求头，并替换其中的标志符号为用户输入的城市
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 客户将要发送给服务器的SOAP请求
	 */
	private static String getSoapRequest(String city) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
				+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>    <getWeather xmlns=\"http://WebXml.com.cn/\">" + "<theCityCode>" + city
				+ "</theCityCode>    </getWeather>" + "</soap:Body></soap:Envelope>");
		return sb.toString();
	}
 
	/**
	 * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 服务器端返回的输入流，供客户端读取
	 * @throws Exception
	 */
	private static InputStream getSoapInputStream(String city) throws Exception {
		try {
			String soap = getSoapRequest(city);
			if (soap == null) {
				return null;
			}
			URL url = new URL("http://www.webxml.com.cn/WebServices/WeatherWS.asmx");
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
 
			conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "http://WebXml.com.cn/getWeather");
 
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			osw.flush();
			osw.close();
 
			InputStream is = conn.getInputStream();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	/**
	 * 对服务器端返回的XML进行解析
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 字符串 用#分割
	 */
	public static String getWeather(String city) {
		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = getSoapInputStream(city);
			doc = db.parse(is);
			NodeList nl = doc.getElementsByTagName("string");
			StringBuffer sb = new StringBuffer();
			for (int count = 0; count < nl.getLength(); count++) {
				Node n = nl.item(count);
				if (n.getFirstChild().getNodeValue().equals("查询结果为空！")) {
					sb = new StringBuffer("#");
					break;
				}
				sb.append(n.getFirstChild().getNodeValue() + "#");
			}
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * 调用获取城市列表接口,返回所有数据
     * @return 返回接口数据
     */
    public static String excute(){
        String url="http://v.juhe.cn/weather/citys?key=***a7558b2e0bedaa19673f74a6809ce";//接口URL
        //PureNetUtil是一个封装了get和post方法获取网络请求数据的工具类
        return PureNetUtil.get(url);//使用get方法
    }
    /**
     * 调用接口返回数据后,解析数据,根据输入城市名得到对应ID
     * @param cityName 城市名称
     * @return 返回对应ID
     */
    public static String getIDBycityName(String cityName) {
        String result=excute();//返回接口结果,得到json格式数据
        if(result!=null){
            JSONObject obj=JSONObject.parseObject(result);
            result=obj.getString("resultcode");//得到返回状态码
            if(result!=null&&result.equals("200")){//200表示成功返回数据
                result=obj.getString("result");//得到城市列表的json格式字符串数组
                JSONArray arr=JSONArray.parseArray(result);
                for(Object o:arr){//对arr进行遍历
                    //将数组中的一个json个数字符串进行解析
                    obj=JSONObject.parseObject(o.toString());
                    /*此时obj如 {"id":"2","province":"北京","city":"北京","district":"海淀"}*/
                    //以city这个key为线索判断所需要寻找的这条记录
                    result=obj.getString("district");
                    //防止输入城市名不全,如苏州市输入为苏州,类似与模糊查询
                    if(result.equals(cityName)||result.contains(cityName)){
                        result=obj.getString("id");//得到ID
                        return result;
                    }
                }
            }
        }
        return result;
    }
	/**
	 * 测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//String weatherInfo = getWeather("深圳");
		String sendGet = PureNetUtil.sendGet("https://www.tianqiapi.com/api/", "version=v1&city=深圳");
		JSONObject weather = JSONObject.parseObject(sendGet);
		System.out.println(sendGet);
		System.out.println(weather);
		JSONArray jsonArray = weather.getJSONArray("data");
		JSONObject jsonObject = weather.getJSONObject("data");
		System.out.println(jsonArray);
		System.out.println(jsonObject);
	}
}
