package dao;

import connection.DBManager;
import entity.Flight;
import org.springframework.stereotype.Component;

import java.sql.*;
@Component
public class FlightDaoImpl implements FlightDao{
    @Override
    public void addFlight(Flight flight) throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="INSERT INTO flight(FlightNo,Airlines,DepartureDate,ArrivalDate,DepartureTime,ArrivalTime) VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        pstmt.setString(1,flight.getFlightNo());
        pstmt.setString(2,flight.getAirlines());
        pstmt.setDate(3,flight.getDepartureDate());
        pstmt.setDate(4,flight.getArrivalDate());
        pstmt.setTime(5,flight.getDepartureTime());
        pstmt.setTime(6,flight.getArrivalTime());
        pstmt.executeUpdate();
        DBManager.closeConnection(conn);
    }

    @Override
    public Flight getFlightByFlightNo(String flightNo) throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight WHERE FlightNo=?";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        pstmt.setString(1,flightNo);
        ResultSet rs=pstmt.executeQuery();
        if(rs.next()){
            String Airlines=rs.getString("Airlines");
            Date DepartureDate=rs.getDate("DepartureDate");
            Date ArrivalDate=rs.getDate("ArrivalDate");
            Time DepartureTime=rs.getTime("DepartureTime");
            Time ArrivalTime=rs.getTime("ArrivalTime");
            Flight flight=new Flight(flightNo,Airlines,DepartureDate,ArrivalDate,DepartureTime,ArrivalTime);
            DBManager.closeConnection(conn);
            return flight;
        }
        DBManager.closeConnection(conn);
        return null;
    }

    @Override
    public void sortFlightsByDepartureDate() throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight ORDER BY DepartureDate";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();
        System.out.println(rs);
        DBManager.closeConnection(conn);
    }

    @Override
    public void sortFlightsByArrivalDate() throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight ORDER BY ArrivalDate";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();
        System.out.println(rs);
        DBManager.closeConnection(conn);
    }

    @Override
    public void sortFlightsByDepartureTime() throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight ORDER BY DepartureTime";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();
        System.out.println(rs);
        DBManager.closeConnection(conn);
    }

    @Override
    public void sortFlightsByArrivalTime() throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight ORDER BY ArrivalTime";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();
        System.out.println(rs);
        DBManager.closeConnection(conn);
    }

    @Override
    public void sortFlightsByDepartureDateAndTime() throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight ORDER BY DepartureDate,DepartureTime";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();
        System.out.println(rs);
        DBManager.closeConnection(conn);
    }

    @Override
    public void sortFlightsByArrivalDateAndTime() throws SQLException {
        Connection conn= DBManager.getConnection();
        String sql="SELECT * FROM flight ORDER BY ArrivalDate,ArrivalTime";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        ResultSet rs=pstmt.executeQuery();
        System.out.println(rs);
        DBManager.closeConnection(conn);
    }
}