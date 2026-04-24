import { type IPais } from '@/shared/model/pais.model';

export interface IProvincia {
  id?: number;
  nombre?: string;
  codigo?: string | null;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  activo?: boolean;
  pais?: IPais;
}

export class Provincia implements IProvincia {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigo?: string | null,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public activo?: boolean,
    public pais?: IPais,
  ) {
    this.activo = this.activo ?? false;
  }
}
