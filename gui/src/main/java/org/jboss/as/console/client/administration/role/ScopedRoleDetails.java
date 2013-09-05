/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.console.client.administration.role;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.administration.role.model.ModelHelper;
import org.jboss.as.console.client.administration.role.model.ScopeType;
import org.jboss.as.console.client.administration.role.model.ScopedRole;
import org.jboss.as.console.client.rbac.StandardRole;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormCallback;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;

/**
 * @author Harald Pehl
 */
public class ScopedRoleDetails implements IsWidget {

    private final RoleAssignmentPresenter presenter;
    private final Form<ScopedRole> form;
    private EnumFormItem<ScopeType> typeItem;
    private MultiselectListBoxItem scopeItem;
    private List<String> hosts;
    private List<String> serverGroups;

    public ScopedRoleDetails(final RoleAssignmentPresenter presenter) {
        this.presenter = presenter;
        this.form = new Form<ScopedRole>(ScopedRole.class);
    }

    @Override
    public Widget asWidget() {
        TextBoxItem nameItem = new TextBoxItem("name", Console.CONSTANTS.common_label_name());
        final EnumFormItem<StandardRole> baseRoleItem = new EnumFormItem<StandardRole>("baseRole",
                Console.CONSTANTS.administration_base_role());
        baseRoleItem.setValues(ModelHelper.roles());
        typeItem = new EnumFormItem<ScopeType>("type", Console.CONSTANTS.common_label_type());
        typeItem.setDefaultToFirst(true);
        typeItem.setValues(ModelHelper.scopes());
        typeItem.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(final ChangeEvent event) {
                updateScope(typeItem.getValue());
            }
        });
        scopeItem = new MultiselectListBoxItem("scope", Console.CONSTANTS.administration_scope(), 3);
        form.setFields(nameItem, baseRoleItem, typeItem, scopeItem);
        form.setEnabled(false);
        form.setToolsCallback(new FormCallback() {
            @Override
            public void onSave(final Map changeset) {
                ScopedRole scopedRole = form.getUpdatedEntity();
                // The form cannot handle enums...
                scopedRole.setBaseRole(baseRoleItem.getValue());
                scopedRole.setType(typeItem.getValue());
                presenter.saveScopedRole(scopedRole, form.getChangedValues());
            }

            @Override
            public void onCancel(final Object entity) {
            }
        });

        VerticalPanel content = new VerticalPanel();
        content.setStyleName("fill-layout-width");
        content.add(form.asWidget());

        return content;
    }

    public void update(final List<String> hosts, final List<String> serverGroups) {
        this.hosts = hosts;
        this.serverGroups = serverGroups;
    }

    @SuppressWarnings("unchecked")
    void bind(CellTable<ScopedRole> table) {
        final SingleSelectionModel<ScopedRole> selectionModel = (SingleSelectionModel<ScopedRole>) table
                .getSelectionModel();
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        ScopedRole role = selectionModel.getSelectedObject();
                        if (role != null) {
                            updateScope(role.getType());
                            form.edit(role);
                        } else {
                            form.clearValues();
                        }
                    }
                });
            }
        });
    }

    private void updateScope(final ScopeType type) {
        if (form != null && typeItem != null && scopeItem != null) {
            if (type == ScopeType.host) {
                scopeItem.setChoices(hosts);
            } else if (type == ScopeType.serverGroup) {
                scopeItem.setChoices(serverGroups);
            }
            // restore selection
            ScopedRole entity = form.getEditedEntity();
            if (entity != null) {
                scopeItem.setValue(entity.getScope());
            }
        }
    }
}