package project.adapter.in.web.Utils;

public record Link(String href,String rel,String type) {
    @Override
    public String toString(){
        return "Link{"+
                "href='"+href+'\''+
                ", rel='"+rel+'\''+
                '}';
    }
}
