import { type IHistoriaClinica } from '@/shared/model/historia-clinica.model';

export interface IAntecedenteClinico {
  id?: number;
  titulo?: string;
  descripcion?: string;
  fechaRegistro?: Date;
  observaciones?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  historiaClinica?: IHistoriaClinica;
}

export class AntecedenteClinico implements IAntecedenteClinico {
  constructor(
    public id?: number,
    public titulo?: string,
    public descripcion?: string,
    public fechaRegistro?: Date,
    public observaciones?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public historiaClinica?: IHistoriaClinica,
  ) {
    this.activo = this.activo ?? false;
  }
}
