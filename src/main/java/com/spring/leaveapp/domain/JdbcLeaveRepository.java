package com.spring.leaveapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcLeaveRepository implements LeaveRepository {

    @Autowired
    JdbcOperations jdbcOperations;

    private final String SAVE_LEAVE = "INSERT INTO " +
            "leaves(employee_id,start_date,end_date,status,type) values(?,?,?,?,?)";
    private final String ALL_PENDING_LEAVES = "SELECT * FROM leaves WHERE status = 'Pending'";
    private final String LEAVE_BY_EMPLOYEE_ID = "SELECT * FROM leaves " +
            "WHERE employee_id = ? AND status = ?";
    private final String ARCHIVED_LEAVES_BY_EMPLOYEE_ID = "SELECT * FROM leaves " +
            "WHERE employee_id = ? AND status = 'Archived'";
    private final String DELETE_LEAVE = "DELETE FROM leaves WHERE leave_id = ?";
    private final String ACTIVE_LEAVE_BY_EMPLOYEE_ID = "SELECT * FROM leaves " +
            "WHERE employee_id = ? AND status <> 'Archived'";
    private final String UPDATE_LEAVE = "UPDATE leaves SET " +
            "start_date = ?, end_date = ?, status = ?, type = ? " +
            "WHERE leave_id = ?";

    @Override
    public void saveLeave(Leave leave) {
        jdbcOperations.update(SAVE_LEAVE,
                leave.getEmployeeId(),
                leave.getFromDate(),
                leave.getToDate(),
                leave.getStatus(),
                leave.getLeaveType());
    }

    @Override
    public List<Leave> findAllPendingLeaves() {
        return jdbcOperations.query(ALL_PENDING_LEAVES,this::mapLeaves);
    }

    @Override
    public Leave findLeaveByEmployeeId(Long employeeId, String status) {
        return jdbcOperations.queryForObject(LEAVE_BY_EMPLOYEE_ID,this::mapLeaves,employeeId,status);
    }

    @Override
    public List<Leave> findArchivedLeavesByEmployeeId(Long employeeId) {
        return jdbcOperations.query(ARCHIVED_LEAVES_BY_EMPLOYEE_ID,this::mapLeaves,employeeId);
    }

    @Override
    public void deleteLeave(Long leaveId) {
        jdbcOperations.update(DELETE_LEAVE,leaveId);
    }

    @Override
    public Leave findActiveLeaveByEmployeeId(Long employeeId) {
        return jdbcOperations.queryForObject(ACTIVE_LEAVE_BY_EMPLOYEE_ID,this::mapLeaves,employeeId);
    }

    @Override
    public void updateLeave(Leave leave) {
        jdbcOperations.update(UPDATE_LEAVE,
                leave.getFromDate(),
                leave.getToDate(),
                leave.getStatus(),
                leave.getLeaveType(),
                leave.getId());
    }

    private Leave mapLeaves(ResultSet rs, int row) throws SQLException {
        return new Leave(rs.getLong("leave_id"),
                rs.getLong("employee_id"),
                rs.getString("start_date"),
                rs.getString("end_date"),
                rs.getString("type"),
                rs.getString("status"));
    }
}
