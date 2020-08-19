package com.projectwild.game.ingame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class GUIWindow {
    
    private static final Color backgroundColor = new Color(71f / 255f, 71f / 255f, 71f / 255f, 1f);

    private GUIHandler parent;
    private DisposeCallback disposeCallback;

    private String name;
    private ArrayList<GUIComponent> components;
    private int x, y, width, height;
    private GUIComponent activeComponent;

    public GUIWindow(String name, DisposeCallback disposeCallback, int x, int y, int width, int height) {
        this.name = name;
        this.disposeCallback = disposeCallback;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void setParent(GUIHandler parent) {
        this.parent = parent;
    }
    
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        int borderWidth = GUIDraw.getRectBorderWidth();
        GUIDraw.drawRect(sb, sr, backgroundColor, x - borderWidth, y - borderWidth, width + borderWidth * 2, height + borderWidth * 2);
        for(GUIComponent component : components)
            component.render(sb, sr);
    }
    
    public void clicked(boolean up, int x, int y) {
        for(GUIComponent component : components) {
            if(x < component.getX() || x > component.getX() + component.getWidth())
                continue;
        
            if(y < component.getY() || y > component.getY() + component.getHeight())
                continue;

            if(up) {
                component.mouseUp(x - component.getX(), y - component.getY());
            } else {
                component.mouseDown(x - component.getX(), y - component.getY());
            }
            if(!up)
                activeComponent = component;
        }
    }

    public void keyTyped(char character) {
        if(activeComponent != null)
            activeComponent.typed(character);
    }
    
    public void dispose() {
        if(disposeCallback != null)
            disposeCallback.onDispose();
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    public GUIComponent getActiveComponent() {
        return activeComponent;
    }

    public GUIHandler getParent() {
        return parent;
    }

    public static class Builder {

        private String name;
        private DisposeCallback disposeCallback;
        private int x, y;
        private int padding;
        private ArrayList<ArrayList<ComponentAlign>> builderComponents;

        public Builder(String name) {
            this.name = name;
            builderComponents = new ArrayList<>();
            builderComponents.add(new ArrayList<>());
            padding = 15;
            x = -1;
            y = -1;
        }

        public Builder add(boolean nextRow, GUIComponent component) {
            return add(nextRow, component, Align.CENTER);
        }

        public Builder add(boolean nextRow, GUIComponent component, GUIWindow.Builder.Align align) {
            if(component == null)
                return this;
            if(nextRow)
                builderComponents.add(new ArrayList<>());
            builderComponents.get(builderComponents.size()-1).add(new ComponentAlign(component, align));
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setY(int y) {
            this.y = y;
            return this;
        }

        public Builder setPadding(int padding) {
            this.padding = padding;
            return this;
        }
        
        public Builder onDispose(DisposeCallback disposeCallback) {
            this.disposeCallback = disposeCallback;
            return this;
        }

        public GUIWindow build() {
            ArrayList<GUIComponent> components = new ArrayList<>();
            int width = 0;
            int height = padding;

            // Gets The Final Width & Height (Needed For Creating Window)
            for(ArrayList<ComponentAlign> row : builderComponents) {
                int rowWidth = 0;
                int rowHeight = 0;
                for(ComponentAlign componentAlign : row) {
                    GUIComponent component = componentAlign.getComponent();
                    rowWidth += component.getWidth() + padding;

                    if(component.getHeight() > rowHeight)
                        rowHeight = component.getHeight();
                }

                // Updating Main Window Width & Height
                height += rowHeight + padding;
                if(rowWidth > width)
                    width = padding + rowWidth;
            }

            if(x == -1 || y == -1) {
                x = Gdx.graphics.getWidth() / 2 - width / 2;
                y = Gdx.graphics.getHeight() / 2 - height / 2;
            }

            GUIWindow window = new GUIWindow(name, disposeCallback, x, y, width, height);

            // Add The Components
            int currentHeight = padding;
            for(ArrayList<ComponentAlign> row : builderComponents) {
                int left = padding;
                int right = padding;
                int center = 0;
                for(ComponentAlign componentAlign : row) {
                    GUIComponent component = componentAlign.getComponent();
                    Align align = componentAlign.getAlign();

                    switch(align) {
                        case LEFT:
                            left += component.getWidth() + padding;
                            break;
                        case RIGHT:
                            right += component.getWidth() + padding;
                            break;
                        case CENTER:
                            center += component.getWidth() + (center != 0 ? padding : 0);
                            break;
                    }
                }

                int centerStartingPoint = left + ((width - left - right) - center) / 2;

                int currentLeft = padding;
                int currentRight = padding;
                int currentCenter = 0;
                int rowHeight = 0;
                for(ComponentAlign componentAlign : row) {
                    GUIComponent component = componentAlign.getComponent();
                    Align align = componentAlign.getAlign();

                    // Getting The X Cord
                    int x;
                    switch(align) {
                        case LEFT:
                            x = currentLeft;
                            currentLeft += component.getWidth() + padding;
                            break;
                        case RIGHT:
                            x = width - right + currentRight;
                            currentRight += component.getWidth() + padding;
                            break;
                        case CENTER:
                            x = centerStartingPoint + currentCenter;
                            currentCenter += component.getWidth() + padding;
                            break;
                        default:
                            x = 0;
                            break;
                    }

                    // Setting The Position & Parent
                    component.setParent(window, window.getX() + x, window.getY() + currentHeight);
                    components.add(component);

                    // Set Row Height
                    if(component.getHeight() > rowHeight)
                        rowHeight = component.getHeight();
                }
                currentHeight += rowHeight + padding;
            }

            window.components = components;

            return window;
        }

        private static class ComponentAlign {

            private GUIComponent component;
            private Align align;

            public ComponentAlign(GUIComponent component, Align align) {
                this.component = component;
                this.align = align;
            }

            public GUIComponent getComponent() {
                return component;
            }

            public Align getAlign() {
                return align;
            }

        }

        public enum Align {
            LEFT,
            RIGHT,
            CENTER;
        }

    }
    
    public interface DisposeCallback {
        void onDispose();
    }
    
}
