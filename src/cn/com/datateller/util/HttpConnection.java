package cn.com.datateller.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpConnection {

	private static final String TAG = "HttpConnection";

	// �ú�����Ҫ�����������֮���ͨ��
	public static InputStream communicateWithServer(String urlString,
			List<NameValuePair> list) {
		/*
		 * 1:��ȡDefaultHttpClient��һ������ 2����ȡUrlEncodedFormEntity��һ������entity
		 * 3�����entity���� 4����ȡpost���� 5��ʹ��clientִ��post���� 6��ʹ��HttpResponse���շ������ķ��ص���
		 */
		DefaultHttpClient client = new DefaultHttpClient();
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(list, "UTF-8");
			HttpPost post = new HttpPost(urlString);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream in = response.getEntity().getContent();
				return in;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// �÷���ͨ��ʹ��ByteArrayOutputStream�����������ص�������Ϊ�ַ���
	public static String readString(InputStream in) throws IOException {
		// TODO Auto-generated method stub
		byte[] data = new byte[128];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			while ((length = in.read(data)) != -1) {
				bout.write(data, 0, length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new String(bout.toByteArray(), "UTF-8");
	}

	public static String InputStreamToString(InputStream in) {
		// TODO Auto-generated method stub
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			int n = 0;
			while ((n = in.read(b)) != -1) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out.toString();
	}

	public static String communicateServerWithGetMethod(String urlString) {
		// TODO Auto-generated method stub
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(urlString);
		HttpResponse response;
		try {
			response = client.execute(httpget);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				/*InputStream in = response.getEntity().getContent();
				return in;*/
				String strResult = EntityUtils.toString(response.getEntity());
				System.out.println(strResult);
				return strResult;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
