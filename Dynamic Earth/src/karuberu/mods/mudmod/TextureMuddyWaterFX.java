package karuberu.mods.mudmod;

import cpw.mods.fml.client.FMLTextureFX;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureMuddyWaterFX extends FMLTextureFX {
    protected float[] red = new float[256];
    protected float[] green = new float[256];
    protected float[] blue = new float[256];
    protected float[] alpha = new float[256];
    private int tickCounter = 0;

    public TextureMuddyWaterFX() {
        super(MudMod.muddyWaterStill.blockIndexInTexture);
        setup();
    }

    @Override
    public void setup() {
        super.setup();
        red = new float[tileSizeSquare];
        green = new float[tileSizeSquare];
        blue = new float[tileSizeSquare];
        alpha = new float[tileSizeSquare];
        tickCounter = 0;
    }
    
    public void onTick() {
        ++this.tickCounter;
        int r;
        int g;
        int b;

        for (int i = 0; i < tileSizeBase; ++i) {
            for (int j = 0; j < tileSizeBase; ++j) {
                float float1 = 0.0F;
                for (int k = i - 1; k <= i + 1; ++k) {
                    r = k & tileSizeMask;
                    g = j & tileSizeMask;
                    float1 += this.red[r + g * tileSizeBase];
                }
                this.green[i + j * tileSizeBase] = float1 / 3.3F + this.blue[i + j * tileSizeBase] * 0.8F;
            }
        }

        for (int i = 0; i < tileSizeBase; ++i) {
            for (int j = 0; j < tileSizeBase; ++j) {
                this.blue[i + j * tileSizeBase] += this.alpha[i + j * tileSizeBase] * 0.05F;
                if (this.blue[i + j * tileSizeBase] < 0.0F) {
                    this.blue[i + j * tileSizeBase] = 0.0F;
                }
                this.alpha[i + j * tileSizeBase] -= 0.1F;
                if (Math.random() < 0.05D) {
                    this.alpha[i + j * tileSizeBase] = 0.5F;
                }
            }
        }

        float[] green_temp = this.green;
        this.green = this.red;
        this.red = green_temp;

        for (int j = 0; j < tileSizeSquare; ++j) {
            float float1 = this.red[j];
            if (float1 > 1.0F) {
                float1 = 1.0F;
            }
            if (float1 < 0.0F) {
                float1 = 0.0F;
            }

            float float2 = float1 * float1;
            r = (int)(32.0F + float2 * 32.0F);
            g = (int)(50.0F + float2 * 64.0F);
            b = (int)(146.0F + float2 * 50.0F);

            if (this.anaglyphEnabled) {
                int r3D = (r * 30 + g * 59 + b * 11) / 100;
                int g3D = (r * 30 + g * 70) / 100;
                int b3D = (r * 30 + b * 70) / 100;
                r = r3D;
                g = g3D;
                b = b3D;
            }

            this.imageData[j * 4 + 0] = (byte)r;
            this.imageData[j * 4 + 1] = (byte)g;
            this.imageData[j * 4 + 2] = (byte)255;
            this.imageData[j * 4 + 3] = (byte)b;
        }
    }
}
