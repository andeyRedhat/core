package org.jboss.as.console.client.shared.general;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.shared.general.model.SocketBinding;
import org.jboss.as.console.client.widgets.tables.DefaultCellTable;

import java.util.List;

/**
 * @author Heiko Braun
 * @date 6/7/11
 */
public class SocketTable {

    private DefaultCellTable<SocketBinding> table;
    private int portOffset = 0;

    public SocketTable() {
    }

    public SocketTable(int portOffset) {
        this.portOffset = portOffset;
    }

    public Widget asWidget() {

        table = new DefaultCellTable<SocketBinding>(20);

        TextColumn<SocketBinding> nameColumn = new TextColumn<SocketBinding>() {
            @Override
            public String getValue(SocketBinding record) {
                return record.getName();
            }
        };

        TextColumn<SocketBinding> portColumn = new TextColumn<SocketBinding>() {
            @Override
            public String getValue(SocketBinding record) {
                return String.valueOf(record.getPort()+portOffset);
            }
        };

        table.addColumn(nameColumn, "Name");
        table.addColumn(portColumn, "Port");

        return table;
    }

    public CellTable<SocketBinding> getCellTable() {
        return table;
    }

    public void updateFrom(String groupName, List<SocketBinding> bindings) {
        table.setRowCount(bindings.size(), true);
        table.setRowData(0, bindings);

        if(!bindings.isEmpty() && table.getSelectionModel()!=null)
            table.getSelectionModel().setSelected(bindings.get(0), true);
    }
}
