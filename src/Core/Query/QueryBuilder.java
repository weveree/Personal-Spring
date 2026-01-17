package Core.Query;

import java.util.function.Predicate;

public class QueryBuilder {
    String query ="";
    String table;
    String mode;

    public QueryBuilder select()
    {
        this.mode = "select * from ";
        return this;
    }
    public QueryBuilder update()
    {
        this.mode = "udpate ";
        return this;
    }
    public QueryBuilder delete()
    {
        this.mode = "delete from ";
        return this;
    }
    public QueryBuilder from(String table)
    {
        this.table = table+" ";
        return this;
    }

    public QueryBuilder where(String field,String operator,String value)
    {
        if(!query.contains("where"))
            query+="where ";
        query+= field +" "+ operator+" " + value+" ";
        return this;
    }
    public QueryBuilder where(String field,String operator,QueryBuilder value) throws Exception {
        where(field,operator,"("+value.build()+" ) ");
        return this;
    }
    public QueryBuilder subquery(String queryBuilder)
    {
        query+="( "+queryBuilder+" )";
        return this;
    }
    public QueryBuilder and(){
        query+="and ";
        return this;
    }
    public QueryBuilder or(){
        query+="or";
        return this;
    }

    public String build() throws Exception {
        if(mode == null ||table==null)
            throw new Exception("Request not valid");
        if(mode.isEmpty()||table.isEmpty())
            throw new Exception("Request not valid");
        return mode+table+query;
    }
}
