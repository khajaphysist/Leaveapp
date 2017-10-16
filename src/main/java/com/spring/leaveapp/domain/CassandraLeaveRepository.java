package com.spring.leaveapp.domain;

import com.datastax.driver.core.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class CassandraLeaveRepository implements LeaveRepository {

    @Autowired
    CqlOperations cqlOperations;

    private final String SAVE_LEAVE = "INSERT INTO " +
            "leaves(leave_id,employee_id,start_date,end_date,status,type) values(?,?,?,?,?,?)";

    private final String ALL_PENDING_LEAVES = "SELECT * FROM leaves WHERE status = 'Pending' ALLOW FILTERING";

    private final String LEAVE_BY_EMPLOYEE_ID = "SELECT * FROM leaves " +
            "WHERE employee_id = ? AND status = ? ALLOW FILTERING";

    private final String ARCHIVED_LEAVES_BY_EMPLOYEE_ID = "SELECT * FROM leaves " +
            "WHERE employee_id = ? AND status = 'Archived' ALLOW FILTERING";

    private final String DELETE_LEAVE = "DELETE FROM leaves WHERE leave_id = ? ALLOW FILTERING";

    private final String ACTIVE_LEAVE_BY_EMPLOYEE_ID = "SELECT * FROM leaves " +
            "WHERE employee_id = ? AND status IN ('Pending', 'Accepted','Rejected') ALLOW FILTERING";

    private final String UPDATE_LEAVE = "UPDATE leaves SET " +
            "start_date = ?, end_date = ?, status = ?, type = ? " +
            "WHERE leave_id = ? ALLOW FILTERING";

    @Override
    public void saveLeave(Leave leave) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            cqlOperations.execute(SAVE_LEAVE,
                    System.currentTimeMillis(),
                    leave.getEmployeeId(),
                    LocalDate.fromMillisSinceEpoch(dateFormat.parse(leave.getFromDate()).getTime()),
                    LocalDate.fromMillisSinceEpoch(dateFormat.parse(leave.getToDate()).getTime()),
                    leave.getStatus(),
                    leave.getLeaveType());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Leave> findAllPendingLeaves() {
        return cqlOperations.query(ALL_PENDING_LEAVES,leaveRowMapper);
    }

    @Override
    public Leave findLeaveByEmployeeId(Long employeeId, String status) {
        return cqlOperations.queryForObject(LEAVE_BY_EMPLOYEE_ID,leaveRowMapper,employeeId,status);
    }

    @Override
    public List<Leave> findArchivedLeavesByEmployeeId(Long employeeId) {
        return cqlOperations.query(ARCHIVED_LEAVES_BY_EMPLOYEE_ID,leaveRowMapper,employeeId);
    }

    @Override
    public void deleteLeave(Long leaveId) {
        cqlOperations.execute(DELETE_LEAVE,leaveId);
    }

    @Override
    public Leave findActiveLeaveByEmployeeId(Long employeeId) {
        return cqlOperations.queryForObject(ACTIVE_LEAVE_BY_EMPLOYEE_ID,leaveRowMapper,employeeId);
    }

    @Override
    public void updateLeave(Leave leave) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            cqlOperations.execute(UPDATE_LEAVE,
                    LocalDate.fromMillisSinceEpoch(dateFormat.parse(leave.getFromDate()).getTime()),
                    LocalDate.fromMillisSinceEpoch(dateFormat.parse(leave.getToDate()).getTime()),
                    leave.getStatus(),
                    leave.getLeaveType(),
                    leave.getId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private RowMapper<Leave> leaveRowMapper = (rs,row)-> new Leave(
            rs.getLong("leave_id"),
            rs.getLong("employee_id"),
            rs.getString("start_date"),
            rs.getString("end_date"),
            rs.getString("type"),
            rs.getString("status"));
}
