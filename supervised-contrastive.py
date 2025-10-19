import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import Dataset, DataLoader

# ============================================================
# Model: Encoder + Projection Head
# ============================================================
class SupConEncoder(nn.Module):
    def __init__(self, input_dim=768, hidden_dim=512, proj_dim=128):
        super().__init__()
        # Encoder: maps 768 → 512
        self.encoder = nn.Sequential(
            nn.Linear(input_dim, hidden_dim),
            nn.BatchNorm1d(hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, hidden_dim),
            nn.ReLU(),
        )
        # Projection head: maps 512 → 128 (contrastive space)
        self.projector = nn.Sequential(
            nn.Linear(hidden_dim, proj_dim),
            nn.ReLU(),
            nn.Linear(proj_dim, proj_dim)
        )

    def forward(self, x):
        features = self.encoder(x)
        z = F.normalize(self.projector(features), dim=1)
        return z, features


# ============================================================
# Supervised Contrastive Loss
# (from: https://arxiv.org/abs/2004.11362)
# ============================================================
class SupConLoss(nn.Module):
    def __init__(self, temperature=0.07):
        super().__init__()
        self.temperature = temperature

    def forward(self, features, labels):
        """
        features: [batch_size, feature_dim]
        labels: [batch_size]
        """
        device = features.device
        labels = labels.contiguous().view(-1, 1)
        mask = torch.eq(labels, labels.T).float().to(device)

        # Normalize features and compute similarity matrix
        features = F.normalize(features, dim=1)
        logits = torch.div(torch.matmul(features, features.T), self.temperature)

        # Mask self-contrast cases
        logits_mask = torch.ones_like(mask) - torch.eye(mask.shape[0], device=device)
        mask = mask * logits_mask

        # log_prob = log( exp(sim_ij) / sum(exp(sim_ik)) )
        exp_logits = torch.exp(logits) * logits_mask
        log_prob = logits - torch.log(exp_logits.sum(1, keepdim=True) + 1e-12)

        # mean log-likelihood for positive pairs
        mean_log_prob_pos = (mask * log_prob).sum(1) / (mask.sum(1) + 1e-12)

        # loss = - (1/N) * sum(mean_log_prob_pos)
        loss = -mean_log_prob_pos.mean()
        return loss


# ============================================================
# Example Dataset
# ============================================================
class RandomDataset(Dataset):
    def __init__(self, n=1000, input_dim=768, n_classes=10):
        self.x = torch.randn(n, input_dim)
        self.y = torch.randint(0, n_classes, (n,))

    def __len__(self): return len(self.x)
    def __getitem__(self, idx): return self.x[idx], self.y[idx]


# ============================================================
# Training Script
# ============================================================
def train():
    # Data
    dataset = RandomDataset()
    loader = DataLoader(dataset, batch_size=128, shuffle=True)

    # Model & loss
    model = SupConEncoder()
    criterion = SupConLoss(temperature=0.07)
    optimizer = torch.optim.Adam(model.parameters(), lr=1e-3)

    model.train()
    for epoch in range(10):
        total_loss = 0
        for x, y in loader:
            x, y = x.cuda(), y.cuda() if torch.cuda.is_available() else (x, y)
            z, _ = model(x)
            loss = criterion(z, y)

            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

            total_loss += loss.item()

        print(f"Epoch [{epoch+1}/10] | Loss: {total_loss/len(loader):.4f}")


if __name__ == "__main__":
    train()

