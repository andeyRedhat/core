/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
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
package org.jboss.as.console.mbui.model.mapping;

import org.useware.kernel.model.mapping.Mapping;
import org.useware.kernel.model.mapping.MappingType;
import org.useware.kernel.model.structure.QName;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapping for a concrete resource in the DMR model.
 *
 * @author Harald Pehl
 * @date 10/25/2012
 */
public class DMRMapping extends Mapping
{
    private String address;
    private final List<ResourceAttribute> attributes;
    private final List<String> objects;

    private String parentAddress;
    public final static QName ID = new QName("htt://whildfly.org", "dmr");

    public DMRMapping()
    {
        super(MappingType.DMR);
        this.attributes = new ArrayList<ResourceAttribute>();
        this.objects = new ArrayList<String>();
    }

    private DMRMapping(List<ResourceAttribute> attributes, String address) {
        super(MappingType.DMR);
        this.attributes = attributes;
        this.objects = new ArrayList<String>();
        this.address = address;
    }

    public DMRMapping setAddress(String address)
    {
        assert address != null : "Address must not be null";
        this.address = address;
        return this;
    }

    public DMRMapping addAttributes(final List<String> attributes) {
        for (String attribute : attributes)
        {
            if (attribute != null && attribute.length() != 0)
            {
                this.attributes.add(new ResourceAttribute(attribute));
            }
        }
        return this;
    }

    public DMRMapping addAttributes(final String... attributes)
    {
        for (String attribute : attributes)
        {
            if (attribute != null && attribute.length() != 0)
            {
                this.attributes.add(new ResourceAttribute(attribute));
            }
        }
        return this;
    }

    /**
     * @see #complementFrom(org.useware.kernel.model.mapping.Mapping)
     * @return either the local address or the one resolved from the parent
     */
    public String getResolvedAddress() {
        return parentAddress !=null ? parentAddress : address;
    }

    // TODO: Required? What's the purpose?
    public DMRMapping addAttribute(final ResourceAttribute attribute)
    {
        if (attribute != null)
        {
            this.attributes.add(attribute);
        }
        return this;
    }

    /**
     * the local address
     * @see #getResolvedAddress()
     * @return
     */
    public String getAddress()
    {
        return address;
    }

    public List<ResourceAttribute> getAttributes()
    {
        return attributes;
    }

    @Override
    public void complementFrom(Mapping parent) {
        if(parent instanceof DMRMapping)
        {
            DMRMapping parentDMRMapping = (DMRMapping)parent;

            // complementFrom address if not available
            if(null==this.address)
                this.parentAddress = parentDMRMapping.getAddress();
        }
    }

    @Override
    public DMRMapping copy() {
        return new DMRMapping(attributes, address);
    }

    @Override
    public QName getId() {
        return ID;
    }

    // TODO method name: do we stick with 'objects'?
    public void addObjects(List<String> objects) {
        this.objects.addAll(objects);
    }

    public List<String> getObjects() {
        return objects;
    }
}
