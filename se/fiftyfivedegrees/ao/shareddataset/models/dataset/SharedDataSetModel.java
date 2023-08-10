// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models.dataset;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import se.fiftyfivedegrees.ao.shareddataset.entities.SharedDataset;
import se.fiftyfivedegrees.ao.shareddataset.models.OwnerModel;
import se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes.Filters;
import se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes.Columns;
import java.util.HashMap;
import se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes.FieldList;
import java.util.List;
import se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes.DataConfigs;
import javax.xml.bind.annotation.XmlAttribute;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SharedDataSet")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SharedDataSetModel
{
    @XmlAttribute(name = "id")
    private Integer id;
    @XmlAttribute(name = "uuid")
    private String uuid;
    @XmlAttribute(name = "shared")
    private Boolean shared;
    @XmlAttribute(name = "starred")
    private Boolean starred;
    @XmlAttribute(name = "admin")
    private Boolean admin;
    @XmlAttribute(name = "owner")
    private String owner;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "date")
    private String date;
    @XmlAttribute(name = "dataConfigs")
    private DataConfigs dataConfigs;
    @XmlAttribute(name = "fieldList")
    private List<FieldList> fieldList;
    @XmlAttribute(name = "columns")
    private HashMap<String, Columns> columns;
    @XmlAttribute(name = "columnOrder")
    private List<String> columnOrder;
    @XmlAttribute(name = "filters")
    private Filters filters;
    @XmlAttribute(name = "owners")
    private List<OwnerModel> owners;
    
    public SharedDataSetModel() {
    }
    
    public SharedDataSetModel(final SharedDataset sharedDataset) {
        final Gson gson = new Gson();
        this.id = sharedDataset.getID();
        this.uuid = sharedDataset.getUuid();
        this.shared = sharedDataset.isShared();
        this.owner = sharedDataset.getOwner();
        this.name = sharedDataset.getName();
        this.date = sharedDataset.getDate();
        this.dataConfigs = gson.fromJson(sharedDataset.getDataConfigs(), DataConfigs.class);
        final TypeToken<List<FieldList>> fieldListType = new TypeToken<List<FieldList>>() {};
        this.fieldList = gson.fromJson(sharedDataset.getFieldList(), fieldListType.getType());
        final TypeToken<HashMap<String, Columns>> columnsType = new TypeToken<HashMap<String, Columns>>() {};
        this.columns = gson.fromJson(sharedDataset.getColumns(), columnsType.getType());
        final TypeToken<List<String>> columnOrderType = new TypeToken<List<String>>() {};
        this.columnOrder = gson.fromJson(sharedDataset.getColumnOrder(), columnOrderType.getType());
        this.filters = gson.fromJson(sharedDataset.getFilters(), Filters.class);
    }
    
    public SharedDataSetModel(final String uuid, final Boolean shared, final Boolean starred, final Boolean admin, final String owner, final String name, final String date, final DataConfigs dataConfigs, final List<FieldList> fieldList, final HashMap<String, Columns> columns, final List<String> columnOrder, final Filters filters) {
        this.uuid = uuid;
        this.shared = shared;
        this.starred = starred;
        this.admin = admin;
        this.owner = owner;
        this.name = name;
        this.date = date;
        this.dataConfigs = dataConfigs;
        this.fieldList = fieldList;
        this.columns = columns;
        this.columnOrder = columnOrder;
        this.filters = filters;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }
    
    public Boolean isShared() {
        return this.shared;
    }
    
    public void setShared(final Boolean shared) {
        this.shared = shared;
    }
    
    public Boolean isStarred() {
        return this.starred;
    }
    
    public void setStarred(final Boolean starred) {
        this.starred = starred;
    }
    
    public Boolean isAdmin() {
        return this.admin;
    }
    
    public void setAdmin(final Boolean admin) {
        this.admin = admin;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public void setDate(final String date) {
        this.date = date;
    }
    
    public DataConfigs getDataConfigs() {
        return this.dataConfigs;
    }
    
    public void setDataConfigs(final DataConfigs dataConfigs) {
        this.dataConfigs = dataConfigs;
    }
    
    public HashMap<String, Columns> getColumns() {
        return this.columns;
    }
    
    public void setColumns(final HashMap<String, Columns> columns) {
        this.columns = columns;
    }
    
    public List<FieldList> getFieldList() {
        return this.fieldList;
    }
    
    public void setFieldList(final List<FieldList> fieldList) {
        this.fieldList = fieldList;
    }
    
    public List<String> getColumnOrder() {
        return this.columnOrder;
    }
    
    public void setColumnOrder(final List<String> columnOrder) {
        this.columnOrder = columnOrder;
    }
    
    public Filters getFilters() {
        return this.filters;
    }
    
    public void setFilters(final Filters filters) {
        this.filters = filters;
    }
    
    public List<OwnerModel> getOwners() {
        return this.owners;
    }
    
    public void setOwners(final List<OwnerModel> owners) {
        this.owners = owners;
    }
}
