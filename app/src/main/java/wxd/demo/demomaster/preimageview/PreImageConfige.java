package wxd.demo.demomaster.preimageview;

//预览图片基本配置器,包括设置图片加载器，即实现一个继承PreImageLoader即可
public class PreImageConfige {

    private static PreImageConfige preImageConfige;
    private PreImageLoader preImageLoader;

    private PreImageConfige() {
    }

    public static PreImageConfige getInstance() {
        if (preImageConfige == null) {
            synchronized (PreImageConfige.class) {
                if (preImageConfige == null) {
                    preImageConfige = new PreImageConfige();
                }
            }
        }
        return preImageConfige;
    }

    public void setImageLoader(PreImageLoader preImageLoader){
        this.preImageLoader = preImageLoader;
    }

    public PreImageLoader getPreImageLoader() {
        return preImageLoader;
    }

}
