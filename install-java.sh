#!/bin/bash

echo "🚀 Quiz Game - Java Installation Helper"
echo "========================================"
echo ""

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "📦 Homebrew not found. Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    
    # Add Homebrew to PATH for Apple Silicon Macs
    if [[ $(uname -m) == 'arm64' ]]; then
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
        eval "$(/opt/homebrew/bin/brew shellenv)"
    fi
else
    echo "✅ Homebrew is already installed"
fi

# Install Java 17
echo ""
echo "☕ Installing Java 17..."
brew install openjdk@17

# Link Java
echo ""
echo "🔗 Setting up Java..."
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Add to PATH
echo ""
echo "📝 Adding Java to PATH..."
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc

# Verify
echo ""
echo "✅ Installation complete! Verifying..."
/opt/homebrew/opt/openjdk@17/bin/java --version

echo ""
echo "🎉 Setup complete!"
echo ""
echo "Next steps:"
echo "1. Restart your terminal or run: source ~/.zshrc"
echo "2. Run the app: cd ~/Desktop/Quiz-Game && ./mvnw spring-boot:run"

