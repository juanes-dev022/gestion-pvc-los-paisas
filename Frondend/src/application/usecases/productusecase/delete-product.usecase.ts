import type { IProductGateway } from '$application/interfaces/iproduct-gateway'

export class DeleteProductUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(id: string): Promise<void> {
    if (id == null) throw new Error('Id requerido')
    await this.gateway.delete(id)
  }
}
