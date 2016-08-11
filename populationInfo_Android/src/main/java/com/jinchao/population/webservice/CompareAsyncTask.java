package com.jinchao.population.webservice;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 上传图片进行比较线程
 * 
 * @author Administrator
 *
 */
public class CompareAsyncTask extends AsyncTask<Object, Object, CompareResult> {

	// 调用的方法
	// 192.168.1.18:4096
	// 112.4.150.76:4096
	// 192.168.1.80:4096
	private static final String SERVICE_URL = "http://112.4.150.76:4096";
	private static final String SERVICE_NS = "urn:fcsm";
	private static final String methodName = "FcsPhotoCompare";

	private static final String TAG = CompareAsyncTask.class.getSimpleName();

	private byte[] imgA;
	private byte[] imgB;

	public CompareAsyncTask(byte[] imgA, byte[] imgB) {
		super();
		this.imgA = imgA;
		this.imgB = imgB;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected CompareResult doInBackground(Object... params) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// 4.0以后需要加入下列两行代码，才可以访问Web Service
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());

			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		// 创建httpTransportSE传输对象
		HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
		ht.debug = true;
		// 使用soap1.0协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		// 实例化SoapObject对象
		SoapObject request = new SoapObject(SERVICE_NS, methodName);
		Log.e(TAG, "imgA长度：" + imgA.length);
		Log.e(TAG, "imgB长度：" + imgB.length);
		String iA = new String(Base64.encode(imgA));
		String iB = new String(Base64.encode(imgB));
		request.addProperty("imgA", new SoapPrimitive(SoapEnvelope.ENC, "base64Binary", iA));
		request.addProperty("imgB", new SoapPrimitive(SoapEnvelope.ENC, "base64Binary", iB));
		// 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		envelope.bodyOut = request;
		try {
			// 调用webService的方法
			ht.call("", envelope);
			if (envelope.getResponse() != null) {
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject soapObject = (SoapObject) result.getProperty(0);
				// 按照顺序
				int retcode = Integer.parseInt(soapObject.getProperty(0).toString());
				int score = Integer.parseInt(soapObject.getProperty(1).toString());
				return new CompareResult(retcode, score);
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}
}
