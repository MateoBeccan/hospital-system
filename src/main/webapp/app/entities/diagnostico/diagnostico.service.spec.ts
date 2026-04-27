import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Diagnostico } from '@/shared/model/diagnostico.model';

import DiagnosticoService from './diagnostico.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Diagnostico Service', () => {
    let service: DiagnosticoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new DiagnosticoService();
      currentDate = new Date();
      elemDefault = new Diagnostico(123, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', false, currentDate, false, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaDiagnostico: dayjs(currentDate).format(DATE_FORMAT),
          fechaResolucion: dayjs(currentDate).format(DATE_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Diagnostico', async () => {
        const returnedFromService = {
          id: 123,
          fechaDiagnostico: dayjs(currentDate).format(DATE_FORMAT),
          fechaResolucion: dayjs(currentDate).format(DATE_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaDiagnostico: currentDate,
          fechaResolucion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Diagnostico', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Diagnostico', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          fechaDiagnostico: dayjs(currentDate).format(DATE_FORMAT),
          descripcion: 'BBBBBB',
          observaciones: 'BBBBBB',
          activo: true,
          fechaResolucion: dayjs(currentDate).format(DATE_FORMAT),
          esPrincipal: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = {
          fechaDiagnostico: currentDate,
          fechaResolucion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Diagnostico', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Diagnostico', async () => {
        const patchObject = {
          observaciones: 'BBBBBB',
          activo: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...new Diagnostico(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          fechaDiagnostico: currentDate,
          fechaResolucion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Diagnostico', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Diagnostico', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          fechaDiagnostico: dayjs(currentDate).format(DATE_FORMAT),
          descripcion: 'BBBBBB',
          observaciones: 'BBBBBB',
          activo: true,
          fechaResolucion: dayjs(currentDate).format(DATE_FORMAT),
          esPrincipal: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaDiagnostico: currentDate,
          fechaResolucion: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Diagnostico', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Diagnostico', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Diagnostico', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
