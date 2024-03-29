package ex.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.id.GUIDGenerator;

import ex.model.County;
import ex.model.CountyDAO;
import ex.model.OrderPackInfo;

@WebServlet("/CountyServlet")
public class CountyServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CountyServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		String requestCity = request.getParameter("city");
		String countys = "";

		try {

			CountyDAO dao = new CountyDAO();
			List<County> countyList = dao.findByProperty("cityid", requestCity);

			if (countyList.isEmpty() || countyList == null ) {

				countys = "[{\"name\":\"稍后开通\",\"number\":\"0315\"}]";
			} else {

				countys = countyList.get(0).getCounty();
			}

			out.print(countys);

			System.out.println(countys);
			
			Session session = dao.getSession();
			Transaction tx = session.beginTransaction();
			OrderPackInfo oInfo = new OrderPackInfo();
			oInfo.setOrderid(UUID.randomUUID().toString());

			session.saveOrUpdate(oInfo);
			tx.commit();
	 

		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	@Override
	public void init() throws ServletException {
		// Put your code here
	}

}
