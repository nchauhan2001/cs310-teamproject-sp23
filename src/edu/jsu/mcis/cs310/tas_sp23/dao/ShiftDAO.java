package edu.jsu.mcis.cs310.tas_sp23.dao;

import java.sql.*;
import java.util.*;
import java.time.*;
import edu.jsu.mcis.cs310.tas_sp23.Badge;
import edu.jsu.mcis.cs310.tas_sp23.EventType;
import edu.jsu.mcis.cs310.tas_sp23.Punch;
import edu.jsu.mcis.cs310.tas_sp23.Shift;

public class ShiftDAO {

    private static final String QUERY_FIND = "SELECT * FROM shift WHERE id = ?";
    private static final String QUERY_BADGE = "SELECT * FROM employee WHERE badgeid = ?";
    private final DAOFactory daoFactory;

    ShiftDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    public Shift find(int id) {
        Shift s = null;
        // find() method code will go here. - William H.
        return s;
    }
}
