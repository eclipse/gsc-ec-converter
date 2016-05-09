import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;
import com.gs.somepackage.api.SomeApi;

public class TestClass
{
    private MutableList<String> list;
    public TestClass()
    {
        this.list = Lists.mutable.empty();
    }
}