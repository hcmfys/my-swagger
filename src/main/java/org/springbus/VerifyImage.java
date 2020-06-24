package org.springbus;

import lombok.Data;

@Data
public class VerifyImage {
    String srcImage;
    String cutImage;
    Integer XPosition;
    Integer YPosition;

    public VerifyImage(String srcImage, String cutImage, int XPosition, int YPosition) {
        this.srcImage = srcImage;
        this.cutImage = cutImage;
        this.XPosition = XPosition;
        this.YPosition = YPosition;
    }
}
